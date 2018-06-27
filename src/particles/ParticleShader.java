package particles;

import org.lwjgl.util.vector.Matrix4f;

import shaders.ShaderProgram;

public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/particles/particleVShader.txt";
	private static final String FRAGMENT_FILE = "/particles/particleFShader.txt";

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		super.addUniform("projectionMatrix");
		super.addUniform("numberOfRows");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "modelViewMatrix");
		super.bindAttribute(5, "texOffset");
		super.bindAttribute(6, "blendFactor");
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix("projectionMatrix", projectionMatrix);
	}
	protected void loadNumberOfRows(float numberOfRows){
		super.loadFloat("numberOfRows", numberOfRows);
	}
}
