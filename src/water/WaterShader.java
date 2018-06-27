package water;

import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;
import toolbox.Maths;
import entities.Camera;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "/water/waterVertex.txt";
	private final static String FRAGMENT_FILE = "/water/waterFragment.txt";

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		addUniform("projectionMatrix");
		addUniform("viewMatrix");
		addUniform("modelMatrix");
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix("projectionMatrix", projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix("viewMatrix", viewMatrix);
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		super.loadMatrix("modelMatrix", modelMatrix);
	}

}
