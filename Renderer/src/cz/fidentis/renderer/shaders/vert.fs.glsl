#version 330

out vec4 fragColor;

in vec3 vertColor;
in vec3 vertNormal;

void main() {
    vec3 n = normalize(vertNormal);
    vec3 light = vec3(0,0,-1);

    float d = dot(n, light) * 0.8;
    fragColor = vec4(vertColor*d, 1);
}