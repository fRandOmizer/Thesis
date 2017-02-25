#version 330

in vec3 position;
in vec3 normal;
in vec3 color;

out vec3 vertColor;
out vec3 vertNormal;

uniform mat4 MVP;
uniform mat3 N;

void main() {
    vertColor = color;

    vertNormal = N * normal;

    gl_Position = MVP * vec4(position, 1.0);
}

