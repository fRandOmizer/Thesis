#version 330

out vec4 fragColor;

in vec4 vertColor;

void main() {
    fragColor = vec4(vertColor);
}