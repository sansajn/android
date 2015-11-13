#include "lighting_fragment.glsl"

varying vec2 texCoord0;
varying vec3 normal0;
varying vec3 worldPos0;

uniform sampler2D diffuse;
uniform PointLight pointLight;

void main()
{	
	vec4 pointLightColor = calcPointLight(pointLight, normalize(normal0), worldPos0);
	gl_FragColor = texture2D(diffuse, texCoord0) * pointLightColor;
}
