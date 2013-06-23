varying vec3 color;
uniform sampler2D tex;

void main()
{
    vec3 ct,cf;
    vec4 texel;
    float at, af;

    cf = color;
    af = gl_FrontMaterial.diffuse.a;
    texel = texture2D(tex, gl_TexCoord[0].st);

    ct = texel.rgb;
    at = texel.a;
    gl_FragColor = vec4(ct * cf, at * af);
}