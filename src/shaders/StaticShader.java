package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

public class StaticShader extends ShaderProgram{
	public static final String VERTEX_FILE = "/shaders/vertexShader.txt";
	public static final String FRAGMENT_FILE = "/shaders/fragmentShader.txt";
	private static final int MAX_LIGHTS = 4;
	private int[] location_lightPosition;
	private int[] location_lightColour;
	private int[] location_lightAttenuation;
	public StaticShader() {
		super(VERTEX_FILE,FRAGMENT_FILE);
	}
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}
	@Override
	protected void getAllUniformLocations() {
		addUniform("transformationMatrix");
		addUniform("projectionMatrix");
		addUniform("viewMatrix");
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		location_lightAttenuation = new int[MAX_LIGHTS];
		for(int i = 0; i < MAX_LIGHTS;i++){
			location_lightPosition[i] = super.getUniformLocation("lightPosition["+i+"]");
		}
		for(int i = 0; i < MAX_LIGHTS;i++){
			location_lightColour[i] = super.getUniformLocation("lightColour["+i+"]");
		}
		for(int i = 0; i < MAX_LIGHTS;i++){
			location_lightAttenuation[i] = super.getUniformLocation("attenuation["+i+"]");
		}
		addUniform("shineDamper");
		addUniform("reflectivity");
		addUniform("useFakeLighting");
		addUniform("sky_Colour");
		addUniform("numberOfRows");
		addUniform("offset");
		addUniform("plane");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix("transformationMatrix", matrix);
	}
	public void loadClipPlane(Vector4f plane){
		super.loadVector("plane", plane);
	}
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix("projectionMatrix", matrix);
	}
	public void loadViewMatrix(Camera camera){
		super.loadMatrix("viewMatrix", Maths.createViewMatrix(camera));
	}
	public void loadLights(List<Light> lights){
		for(int i=0;i<MAX_LIGHTS;i++){
			if(i<lights.size()){
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColour[i], lights.get(i).getColour());
				super.loadVector(location_lightAttenuation[i], lights.get(i).getAttenuation());
			}else{
				super.loadVector(location_lightPosition[i], new Vector3f(0,0,0));
				super.loadVector(location_lightColour[i], new Vector3f(0,0,0));
				super.loadVector(location_lightAttenuation[i], new Vector3f(1,0,0));
			}
		}
	}
	public void loadShineVariables(float damper, float reflectivity){
		super.loadFloat("shineDamper", damper);
		super.loadFloat("reflectivity", reflectivity);
	}
	public void loadFakeLighting(boolean useFakeLighting){
		super.loadBoolean("useFakeLighting", useFakeLighting);
	}
	public void loadSkyColour(float r,float g, float b){
		super.loadVector("sky_Colour", new Vector3f(r,g,b));
	}
	public void loadNumberOfRows(int numberOfRows){
		super.loadFloat("numberOfRows", (float)numberOfRows);
	}
	public void loadOffset(float x, float y){
		super.loadVector("offset", new Vector2f(x,y));
	}
}
