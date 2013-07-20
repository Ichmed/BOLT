varying vec3 color;

void main()
{
    gl_TexCoord[0] = gl_TextureMatrix[0] * gl_MultiTexCoord0; 
    
    vec3 vertexPosition = (gl_ModelViewMatrix * gl_Vertex).xyz;
    vec3 lightDirection = normalize(gl_LightSource[0].position.xyz - vertexPosition);    
    vec3 surfaceNormal = (gl_NormalMatrix * gl_Normal).xyz;
    
    float diffuseLightIntensity = max(0.0, dot(surfaceNormal, lightDirection));
    
    color.rgb = diffuseLightIntensity * gl_FrontMaterial.diffuse.rgb;
    color += gl_FrontMaterial.ambient.rgb + gl_LightModel.ambient.rgb;
    
    vec3 reflectionDirection = normalize(reflect(-lightDirection, surfaceNormal));
    
    float specular = max(0.0, dot(surfaceNormal, reflectionDirection));
    
    if(diffuseLightIntensity != 0.0)
    {
        float fspecular = pow(specular, gl_FrontMaterial.shininess);
        color.rgb += (gl_FrontMaterial.specular.rgb * fspecular);
    }
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}