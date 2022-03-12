#type vertex
#version 330 core
// aSomething -> is an attribute
// fSomething -> used by fragment shader
// uSomething -> uploaded by code

// Basics
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexID;

uniform mat4 uProj;
uniform mat4 uView;
uniform sampler2D uTextures[8];

out vec4 fColor;
out vec2 fTexCoords;
out float fTexID;

void main(){
    fColor      = aColor;
    fTexCoords  = aTexCoords;
    fTexID      = aTexID;

    gl_Position = uProj * uView * vec4(aPos, 1.0);
}

#type fragment
#version 330 core
//uniform float uTime;
//uniform sampler2D MAIN_TEX_SAMPLER;

uniform sampler2D uTextures[8];
in vec4 fColor;
in vec2 fTexCoords;
in float fTexID;
out vec4 color;

void main(){
    if(fTexID > 0){
        int id = int(fTexID);
        color = fColor * texture(uTextures[id], fTexCoords);
        // color = vec4(fTexCoords, 0, 1); UV DEBUG COLORS
    }
    else{
        color = fColor;
    }
}
