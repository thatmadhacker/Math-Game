package skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import renderEngine.DisplayManager;
import shaders.ShaderProgram;
import toolbox.Maths;

public class SkyboxShader extends ShaderProgram {
	private static final String VERTEX_FILE = "/skybox/skyboxVertexShader.txt";
	private static final String FRAGMENT_FILE = "/skybox/skyboxFragmentShader.txt";
	private static final float ROTATE_SPEED = 1f;
	private float currentRotation;
	public SkyboxShader() {
		super(VERTEX_FILE,FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		addUniform("projectionMatrix");
		addUniform("viewMatrix");
		addUniform("cubeMap");
		addUniform("cubeMap2");
		addUniform("blendFactor");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	public void loadViewMatrix(Camera camera){
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		currentRotation += ROTATE_SPEED * DisplayManager.getFrameTimeSeconds();
		Matrix4f.rotate((float) Math.toRadians(currentRotation), new Vector3f(0,1,0), matrix, matrix);
		super.loadMatrix("viewMatrix", matrix);
	}
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix("projectionMatrix", matrix);
	}
	public void loadBlendFactor(float blend){
		super.loadFloat("blendFactor", blend);
	}
	public void connectTextureUnits(){
		super.loadInt("cubeMap", 0);
		super.loadInt("cubeMap2", 1);
	}
}
