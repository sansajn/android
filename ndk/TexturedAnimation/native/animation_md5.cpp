// md5 animacia
#include <string>
#include <vector>
#include <iostream>  // clog
#include <glm/gtc/matrix_inverse.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include "gl/camera.hpp"
#include "gl/colors.hpp"
#include "gles2/program_gles2.hpp"
#include "gles2/animation_gles2.hpp"
#include "gles2/light_gles2.hpp"
#include "androidgl/android_window.hpp"
#include "resource.hpp"

using std::vector;
using std::string;
using glm::vec3;
using glm::vec2;
using glm::mat3;
using glm::mat4;
using glm::radians;
using glm::rotate;
using glm::inverseTranspose;
using gl::camera;
using gles2::animated_model;
using gles2::shader::program;
using gles2::model_loader_parameters;
using gles2::animated_model_from_file;
using gles2::skeletal_animation;
using gles2::phong_light;
using gles2::material_property;

char const * assets_path = "/sdcard/.textured_animation";
char const * mesh_path = "models/bob_lamp/bob_lamp.md5mesh";
char const * anim_path = "models/bob_lamp/bob_lamp.md5anim";


char const * bump_skinned_shader_source = R"(
	#ifdef _VERTEX_
	const int MAX_JOINTS = 36;

	attribute vec3 position;
	attribute vec2 texcoord;
	attribute vec3 normal;
	attribute vec3 tangent;
	attribute vec4 joints;  // skeleton indices
	attribute vec4 weights;

	uniform mat4 local_to_screen;
	uniform mat4 local_to_camera;
	uniform mat3 normal_to_camera;
	uniform vec3 light_direction;
	uniform mat4 skeleton[MAX_JOINTS];  // kostra, ako zoznam transformacii

	varying vec3 l;
	varying vec3 v;
	varying vec2 uv;

	void main()
	{
		mat4 T_skin =
			skeleton[int(joints.x)] * weights.x +
			skeleton[int(joints.y)] * weights.y +
			skeleton[int(joints.z)] * weights.z +
			skeleton[int(joints.w)] * weights.w;

		vec4 n_skin = T_skin * vec4(normal, 0);
		vec4 t_skin = T_skin * vec4(tangent, 0);

		vec3 n = normal_to_camera * n_skin.xyz;
		vec3 t = normal_to_camera * t_skin.xyz;
		vec3 b = cross(n,t);
		mat3 T_tbn = mat3(t,b,n);

		l = normal_to_camera * light_direction * T_tbn;

		vec4 p = local_to_camera * T_skin * vec4(position, 1);
		v = -p.xyz * T_tbn;

		uv = texcoord;
		gl_Position = local_to_screen * T_skin * vec4(position, 1);
	}
	#endif

	#ifdef _FRAGMENT_
	precision mediump float;
	uniform sampler2D diff_tex;  // diffuse texture
	uniform sampler2D norm_tex;  // normal texture
	uniform sampler2D height_tex;  // height texture
	uniform float light_intensity;  // 1.0
	uniform vec3 light_color;  // vec3(1,1,1)
	uniform vec3 material_ambient;  // vec3(.2,.2,.2)
	uniform float material_shininess;  // 64
	uniform float material_intensity;  // 1.0
	uniform vec2 parallax_scale_bias;  // vec2(.04, -.03)

	varying vec3 l;  // light direction in tangent-space
	varying vec3 v;  // view direction in tangent-space
	varying vec2 uv;

	void main()
	{
		vec3 l_norm = normalize(l);
		vec3 v_norm = normalize(v);
		vec3 n = texture2D(norm_tex, uv).xyz * 2.0 - 1.0;
		
		float diff = max(dot(n, l_norm), 0);
		
		vec3 r = normalize(-reflect(l_norm, n));
		float spec = pow(max(dot(r, v_norm), 0), material_shininess) * material_intensity;
		
		float h = texture2D(height_tex, uv).r * parallax_scale_bias.x + parallax_scale_bias.y;  // TODO: toto je dobre ?
		vec2 uv_ = uv + h * v_norm.xy;
		vec4 texel = texture2D(diff_tex, uv_);
		
		gl_FragColor = vec4(
			(material_ambient + (diff+spec)*light_intensity*light_color) * texel.rgb, texel.a);
	}
	#endif
)";


class scene_window : public ui::android_window
{
public:
	using base = ui::android_window;

	scene_window(parameters const & params);
	void display() override;
	void update(float dt) override;
	void input(float dt) override;

private:
	animated_model _mdl;
	program _prog;
	camera _view;
	phong_light _light;
};

scene_window::scene_window(parameters const & params)
	: base{params}
	, _view{radians(70.0f), aspect_ratio(), 0.01, 1000}
{
	std::clog << "scene_window::scene_window()" << std::endl;

	_prog.from_memory(bump_skinned_shader_source);
	std::clog << "  program shader loaded" << std::endl;

	path_manager & pathman = path_manager::ref();
	_mdl = animated_model_from_file(pathman.translate_path(mesh_path));
	_mdl.append_global(new material_property{vec3{.4}, 1.0f, 48.0f});
	std::clog << "  model loaded" << std::endl;

	_mdl.append_animation(skeletal_animation{pathman.translate_path(anim_path)});
	_mdl.animation_sequence(vector<unsigned>{0});
	std::clog << "  animation loaded" << std::endl;

	int position_a = _prog.attribute_location("position");
	int texcoord_a = _prog.attribute_location("texcoord");
	int normal_a = _prog.attribute_location("normal");
//	int tangent_a = _prog.attribute_location("tangent");
	int joints_a = _prog.attribute_location("joints");
	int weights_a = _prog.attribute_location("weights");
	_mdl.attribute_location({position_a, texcoord_a, normal_a, joints_a, weights_a});

	_view.position = vec3{0,3,10};
	_light = phong_light{normalize(vec3{1,2,3})};

	glClearColor(0, 0, 0, 1);

	std::clog << "scene_window::scene_window():done" << std::endl;
}

void scene_window::display()
{
//	glEnable(GL_CULL_FACE);
	glEnable(GL_DEPTH_TEST);
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

	mat4 M = mat4{1};
	M = rotate(mat4{1}, radians(-90.0f), vec3{1,0,0});
	mat4 local_to_camera = _view.view() * M;
	mat4 local_to_screen = _view.projection() * local_to_camera;
	mat3 normal_to_camera = mat3{inverseTranspose(local_to_camera)};

	_prog.use();
	_light.apply(_prog);
	_prog.uniform_variable("local_to_screen", local_to_screen);
	_prog.uniform_variable("local_to_camera", local_to_camera);
	_prog.uniform_variable("normal_to_camera", normal_to_camera);
	_prog.uniform_variable("parallax_scale_bias", vec2{0.04, -.03});
	_prog.uniform_variable("skeleton", _mdl.skeleton());
	_mdl.render(_prog);

	base::display();
}

void scene_window::update(float dt)
{
	base::update(dt);
	_mdl.update(dt);
}

void scene_window::input(float dt)
{
	base::input(dt);
}


scene_window * w = nullptr;

void create(int width, int height)
{
	path_manager::ref().root_path(assets_path);
	w = new scene_window{scene_window::parameters{}.geometry(width, height)};
}

void destroy()
{
	delete w;
	w = nullptr;
}
