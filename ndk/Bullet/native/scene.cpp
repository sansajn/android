// hod kockou na stenu
#include <utility>
#include <vector>
#include <string>
#include <iostream>
#include <glm/gtc/matrix_inverse.hpp>
#include <glm/gtx/transform.hpp>
#include "gl/window.hpp"
#include "gl/camera.hpp"
#include "gl/shapes.hpp"
#include "gles2/mesh_gles2.hpp"
#include "gles2/program_gles2.hpp"
#include "gles2/default_shader_gles2.hpp"
#include "physics/physics.hpp"
#include "androidgl/android_window.hpp"

using std::pair;
using std::make_pair;
using std::shared_ptr;
using std::move;
using std::string;
using std::vector;
using std::cout;
using glm::vec3;
using glm::mat3;
using glm::mat4;
using glm::radians;
using glm::inverseTranspose;
using glm::normalize;
using glm::scale;
using gl::make_quad_xy;
using gl::make_quad_xz;
using gl::camera;
using gles2::mesh;
using gles2::shader::program;

using namespace phys;


class scene_window : public ui::android_window
{
public:
	using base = ui::android_window;

	scene_window(parameters const & params);
	void input(float dt) override;
	void update(float dt) override;
	void display() override;
	void reshape(int w, int h) override;

private:
	void fire();

	mesh _wall;
	program _prog;
	camera _cam;

	// physics
	rigid_body_world _world;
	body_object _phys_wall;
	vector<body_object> _projectiles;
	shared_ptr<btCollisionShape> _projectile_shape;
};

scene_window::scene_window(parameters const & params)
	: base{params}
{
	_wall = make_quad_xy<mesh>();

	_prog.from_memory(gles2::flat_shaded_shader_source);
	std::clog << "shader program loaded : ok" << std::endl;

	_phys_wall = body_object{make_box_shape(btVector3{3, 3, 0.1})};
	_world.link(_phys_wall);
	_world.native()->setGravity(btVector3{0,0,0});  // vypnem gravitaciu

	_projectile_shape = make_box_shape(btVector3{0.05, 0.05, 0.05});

	glClearColor(0, 0, 0, 1);
}

void scene_window::reshape(int w, int h)
{
	base::reshape(w, h);  // updatne nastavenia okna
	_cam = camera{radians(70.0f), aspect_ratio(), 0.01, 1000};
	_cam.position = vec3{0, 1, 5};
}

void scene_window::fire()
{
	auto projectile = body_object{_projectile_shape, 1, bullet_cast(_cam.position)};
	projectile.native()->setLinearVelocity(bullet_cast(_cam.forward()*-2.0f));
	_world.link(projectile);
	_projectiles.push_back(move(projectile));
}

void scene_window::input(float dt)
{
//	if (in.key_up(' '))
//		fire();
	base::input(dt);
}

void scene_window::update(float dt)
{
	base::update(dt);
	_world.update(dt);
}

void scene_window::display()
{
	vec3 const light_pos{1,3,1};

	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

	mat4 M = scale(vec3{10,10,10});
	mat4 local_to_screen = _cam.view_projection() * M;
	mat3 normal_to_world = mat3{inverseTranspose(mat4{1})};

	_prog.use();
	_prog.uniform_variable("local_to_screen", local_to_screen);
	_prog.uniform_variable("normal_to_world", normal_to_world);
	_prog.uniform_variable("light_dir", normalize(light_pos));
	glEnable(GL_CULL_FACE);
	glEnable(GL_DEPTH_TEST);
	_wall.render();

	_world.debug_render(_cam.view_projection());

	base::display();
}


scene_window * w = nullptr;


void create(int width, int height)
{
	std::clog << "create(w:" << width << ", h:" << height << ")" << std::endl;
	assert(!w && "scene already created");
	w = new scene_window{scene_window::parameters{}.geometry(width, height)};
}

void destroy()
{
	std::clog << "destroy()" << std::endl;
	delete w;
	w = nullptr;
}
