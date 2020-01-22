获取GLSL中的参数
1. uniform mat4 param -> 
GLES20.glGetUniformLocation(program,"param")
2. layout (location = 0) in vec4 vPosition -> 
GLES20.glGetAttribLocation(program,"vPosition")
