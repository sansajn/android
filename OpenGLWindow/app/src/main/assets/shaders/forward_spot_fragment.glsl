#include "lighting_fragment.glsl"

varying vec2 texCoord0;
varying vec3 normal0;
varying vec3 worldPos0;

uniform sampler2D diffuse;
uniform SpotLight spotLight;

void main()
{
	vec4 spotLightColor = calcSpotLight(spotLight, normalize(normal0), worldPos0);
	gl_FragColor = texture2D(diffuse, texCoord0) * spotLightColor;
}
