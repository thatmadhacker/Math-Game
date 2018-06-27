package fontRendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import shaders.ShaderProgram;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "/fontRendering/fontVertex.txt";
	private static final String FRAGMENT_FILE = "/fontRendering/fontFragment.txt";
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		addUniform("colour");
		addUniform("translation");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	protected void loadColour(Vector3f colour){
		super.loadVector("colour", colour);
	}
	
	protected void loadTranslation(Vector2f translation){
		super.loadVector("translation", translation);
	}

}
