#version 330

in vec3 position;
in vec4 color;

out vec4 vertColor;


// width/height
uniform float aspect;
uniform mat4 MVP;
uniform float len;

void main() {
    vertColor = color;

    gl_PointSize = 5.0;
    vec4 pos = vec4(position, 1.0);
    
    pos = MVP * pos;

    gl_Position = pos;
}