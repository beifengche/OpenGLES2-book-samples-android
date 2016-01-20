/*
 * *
 * Copyright 2015 James Andreas
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 */

//
// Book:      OpenGL(R) ES 2.0 Programming Guide
// Authors:   Aaftab Munshi, Dan Ginsburg, Dave Shreiner
// ISBN-10:   0321502795
// ISBN-13:   9780321502797
// Publisher: Addison-Wesley Professional
// URLs:      http://safari.informit.com/9780321563835
//            http://www.opengles-book.com
//

// Simple_VertexShader
//
//    This is a simple example that draws a rotating cube in perspective
//    using a vertex shader to transform the object
//

package com.appendix.teapot_es20;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import com.appendix.common.ESShader;
import com.appendix.common.ESTransform;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TeapotVertexShaderRenderer implements GLSurfaceView.Renderer
{
    // Handle to a program object
    private int mProgramObject;

    // Attribute locations
    private int mPositionLoc;

    // Uniform locations
    private int mMVPLoc;

    // Vertex data
    // private ESShapes mCube = new ESShapes();
    private TeapotData mTeapot;


    // Rotation angle
    private float mAngle;

    // MVP matrix
    private ESTransform mMVPMatrix = new ESTransform();

    // Additional Member variables
    private int mWidth;
    private int mHeight;
    private long mLastTime = 0;

    ///
    // Constructor
    //
    public TeapotVertexShaderRenderer(Context context) {
        mTeapot = new TeapotData(context);
    }

    ///
    // Initialize the shader and program object
    //
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {
        String vShaderStr = "uniform mat4 u_mvpMatrix;                   \n"
                + "attribute vec4 a_position;                  \n"
                + "void main()                                 \n"
                + "{                                           \n"
                + "   gl_Position = u_mvpMatrix * a_position;  \n"
                + "}                                           \n";

        String fShaderStr = "precision mediump float;                            \n"
                + "void main()                                         \n"
                + "{                                                   \n"
                + "  gl_FragColor = vec4( 0.5, 0.0, 0.0, 1.0 );        \n"
                + "}                                                   \n";



//        String fShaderStr =
//                "precision mediump float;" +
//                        "uniform vec4 vColor;" +
//                        "void main() {" +
//                        "  gl_FragColor = vColor;" +
//                        "}";


        // green but don't actually see any gradient
        // http://www.raywenderlich.com/70208/opengl-es-pixel-shaders-tutorial

//        String fShaderStr = "precision mediump float;                            \n"
//                + "uniform vec2 uResolution;                           \n"
//                + "void main()                                         \n"
//                + "{                                                   \n"
//                + "  vec2 position = gl_FragCoord.xy/uResolution;      \n"
//                + "  float gradient = position.x;                      \n"
//                + "  gl_FragColor = vec4(0., gradient, 0., 1.);        \n"
//                + "}                                                   \n";


//        String fShaderStr = "precision mediump float;                            \n"
//                + "uniform vec2 uResolution;                           \n"
//                + "float randomNoise(vec2 p) {     \n"
//                + "  return fract(6791.*sin(47.*p.x+p.y*9973.));     \n"
//                + "}     \n"
//                + "float smoothNoise(vec2 p) {       \n"
//                + "  vec2 nn = vec2(p.x, p.y+1.);       \n"
//                + "  vec2 ne = vec2(p.x+1., p.y+1.);       \n"
//                + "  vec2 ee = vec2(p.x+1., p.y);       \n"
//                + "  vec2 se = vec2(p.x+1., p.y-1.);       \n"
//                + "  vec2 ss = vec2(p.x, p.y-1.);       \n"
//                + "  vec2 sw = vec2(p.x-1., p.y-1.);       \n"
//                + "  vec2 ww = vec2(p.x-1., p.y);       \n"
//                + "  vec2 nw = vec2(p.x-1., p.y+1.);       \n"
//                + "  vec2 cc = vec2(p.x, p.y);       \n"
//                + "        \n"
//                + "  float sum = 0.;       \n"
//                + "  sum += randomNoise(nn);       \n"
//                + "  sum += randomNoise(ne);       \n"
//                + "  sum += randomNoise(ee);       \n"
//                + "  sum += randomNoise(se);       \n"
//                + "  sum += randomNoise(ss);       \n"
//                + "  sum += randomNoise(sw);       \n"
//                + "  sum += randomNoise(ww);       \n"
//                + "  sum += randomNoise(nw);       \n"
//                + "  sum += randomNoise(cc);       \n"
//                + "  sum /= 9.;       \n"
//                + "        \n"
//                + "  return sum;       \n"
//                + "}       \n"
//                + "void main()                                         \n"
//                + "{                                                   \n"
//                + "  vec2 position = gl_FragCoord.xy/uResolution.xx;             \n"
//                + "  float tiles = 128.;             \n"
//                + "  position = floor(position*tiles);             \n"
//                + "  float n = smoothNoise(position);             \n"
//                + "  gl_FragColor = vec4(vec3(n), 1.);             \n"
//                + "}                                                   \n";


        // Load the shaders and get a linked program object
        mProgramObject = ESShader.loadProgram(vShaderStr, fShaderStr);

        // Get the attribute locations
        mPositionLoc = GLES20.glGetAttribLocation(mProgramObject, "a_position");

        // Get the uniform locations
        mMVPLoc = GLES20.glGetUniformLocation(mProgramObject, "u_mvpMatrix");

//        int mColorHandle;
//        float color[] = {0.5f, 0f, 0f};
//        // get handle to fragment shader's vColor member
//        mColorHandle = GLES20.glGetUniformLocation(mProgramObject, "vColor");
//
//        // Set color for drawing the triangle
//        GLES20.glUniform4fv(mColorHandle, 1, color, 0);


        // Generate the vertex data
        mTeapot.genTeapot(5.0f);

        // Starting rotation angle for the cube
        mAngle = 45.0f;

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    private void update()
    {
        if (mLastTime == 0)
            mLastTime = SystemClock.uptimeMillis();
        long curTime = SystemClock.uptimeMillis();
        long elapsedTime = curTime - mLastTime;
        float deltaTime = elapsedTime / 1000.0f;
        mLastTime = curTime;

        ESTransform perspective = new ESTransform();
        ESTransform modelview = new ESTransform();
        float aspect;

        // Compute a rotation angle based on time to rotate the cube
        mAngle += (deltaTime * 40.0f);
        if (mAngle >= 360.0f)
            mAngle -= 360.0f;

        // Compute the window aspect ratio
        aspect = (float) mWidth / (float) mHeight;

        // Generate a perspective matrix with a 60 degree FOV
        perspective.matrixLoadIdentity();
        perspective.perspective(60.0f, aspect, 1.0f, 20.0f);

        // Generate a model view matrix to rotate/translate the cube
        modelview.matrixLoadIdentity();

        // Translate away from the viewer
        modelview.translate(0.0f, 0.0f, -2.0f);

        // Rotate the cube
        modelview.rotate(mAngle, 1.0f, 0.0f, 1.0f);

        // Compute the final MVP by multiplying the
        // modelview and perspective matrices together
        mMVPMatrix.matrixMultiply(modelview.get(), perspective.get());
    }

    ///
    // Draw a cube using the shader pair created in onSurfaceCreated()
    //
    public void onDrawFrame(GL10 glUnused)
    {
        update();

        // Set the viewport
        GLES20.glViewport(0, 0, mWidth, mHeight);

        // Clear the color buffer
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Use the program object
        GLES20.glUseProgram(mProgramObject);

        // Load the vertex data
        GLES20.glVertexAttribPointer(mPositionLoc, 3, GLES20.GL_FLOAT, false,
                0, mTeapot.getVertices());
        GLES20.glEnableVertexAttribArray(mPositionLoc);

        // Load the MVP matrix
        GLES20.glUniformMatrix4fv(mMVPLoc, 1, false,
                mMVPMatrix.getAsFloatBuffer());

//        // Draw the cube
//        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mTeapot.getNumIndices(),
//                GLES20.GL_UNSIGNED_SHORT, mTeapot.getIndices());

        int i = 0;
        int start = 0;
        int num_teapot_indices = mTeapot.getNumIndices();
        ShortBuffer teapot_indices = mTeapot.getIndices();

        while (i < num_teapot_indices) {
            if (teapot_indices.get(i) == -1) {
                GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, i - start,
                        GLES20.GL_UNSIGNED_SHORT, teapot_indices.position(start));
                start = i + 1;
            }
            i++;
        }
        if (start < num_teapot_indices) {
            GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, i - start -1,
                    GLES20.GL_UNSIGNED_SHORT, teapot_indices.position(start));
        }
    }

    ///
    // Handle surface changes
    //
    public void onSurfaceChanged(GL10 glUnused, int width, int height)
    {
        mWidth = width;
        mHeight = height;
    }
}
