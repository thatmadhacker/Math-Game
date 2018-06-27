package shaders;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public abstract class ShaderProgram {
	protected HashMap<String, Integer> uniforms = new HashMap<String,Integer>();
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	public ShaderProgram(String vertexFile,String fragmentFile){
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	public void start(){
		GL20.glUseProgram(programID);
	}
	public void stop(){
		GL20.glUseProgram(0);
	}
	public void cleanUp(){
		stop();
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteProgram(programID);
		getAllUniformLocations();
	}
	protected void bindAttribute(int attribute, String variableName){
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	protected int getUniformLocation(String uniformName){
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	protected void loadFloat(String name, float value){
		GL20.glUniform1f(uniforms.get(name), value);
	}
	protected void loadVector(String name, Vector3f vector){
		GL20.glUniform3f(uniforms.get(name), vector.x, vector.y, vector.z);
	}
	protected void loadBoolean(String name, boolean value){
		float toLoad = 0;
		if(value){
			toLoad = 1;
		}
		GL20.glUniform1f(uniforms.get(name), toLoad);
	}
	protected void loadMatrix(String name, Matrix4f matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(uniforms.get(name), false, matrixBuffer);
	}
	protected abstract void getAllUniformLocations();
	protected abstract void bindAttributes();
	private static int loadShader(String file, int type){
		StringBuilder shaderSource = new StringBuilder();
		try{
			InputStream in = Class.class.getResourceAsStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line = reader.readLine()) != null){
				shaderSource.append(line).append("\n");
			}
			reader.close();
		}catch(Exception e){
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader!");
			System.exit(-1);
		}
		return shaderID;
			
		}
	protected void addUniform(String name){
		uniforms.put(name,getUniformLocation(name));
	}
	protected void loadInt(String location, int value){
		GL20.glUniform1i(uniforms.get(location), value);
	}
	protected void loadVector(String location, Vector2f value){
		GL20.glUniform2f(uniforms.get(location), value.x, value.y);
	}
	protected void loadVector(int location, Vector3f vector){
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	protected void loadVector(String location, Vector4f value){
		GL20.glUniform4f(uniforms.get(location), value.x, value.y,value.z,value.w);
	}
	}