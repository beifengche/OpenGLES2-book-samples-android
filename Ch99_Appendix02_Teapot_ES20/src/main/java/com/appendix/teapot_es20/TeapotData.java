/*
 *
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

package com.appendix.teapot_es20;

import android.content.Context;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Scanner;

public class TeapotData {
    private Context mContext;
    private FloatBuffer mVertices;
    private FloatBuffer mNormals;
    private FloatBuffer mTexCoords;
    private ShortBuffer mIndices;
    private int mNumIndices;
    private int mNumVertices;
    private int mNumNormals;

    public TeapotData(Context mContext) {
        this.mContext = mContext;
    }

    public int genTeapot(float scale) {
        int i;
        short[] indices;
        float[] normalData;
        float[] vertexData;

//        indices = loadIndices(mContext.getResources().openRawResource(R.raw.indices_cube));
//        normalData = loadFloatArray(mContext.getResources().openRawResource(R.raw.normals_cube));
//        vertexData = loadFloatArray(mContext.getResources().openRawResource(R.raw.vertices_cube));

        indices = loadIndices(mContext.getResources().openRawResource(R.raw.indices));
        normalData = loadFloatArray(mContext.getResources().openRawResource(R.raw.normals));
        vertexData = loadFloatArray(mContext.getResources().openRawResource(R.raw.vertices));

        mNumIndices = indices.length;
        mNumVertices = vertexData.length;
        mNumNormals = normalData.length;

        // note this works for the cube
//        mVertices = ByteBuffer.allocateDirect(mNumIndices * 3 * 4)
//                .order(ByteOrder.nativeOrder()).asFloatBuffer();
//        mNormals = ByteBuffer.allocateDirect(mNumIndices * 3 * 4)
//                .order(ByteOrder.nativeOrder()).asFloatBuffer();
//        mIndices = ByteBuffer.allocateDirect(mNumIndices * 2)
//                .order(ByteOrder.nativeOrder()).asShortBuffer();


        mVertices = ByteBuffer.allocateDirect(mNumVertices * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mNormals = ByteBuffer.allocateDirect(mNumNormals * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mIndices = ByteBuffer.allocateDirect(mNumIndices * 2)
                .order(ByteOrder.nativeOrder()).asShortBuffer();

//        mVertices.put(vertexData).position(0);
//        for (i = 0; i < mNumIndices * 3; i++) {
//            mVertices.put(i, mVertices.get(i) * scale);
//        }
//        mNormals.put(normalData).position(0);
//        mIndices.put(indices).position(0);

        mVertices.put(vertexData).position(0);
        for (i = 0; i < mNumVertices; i++) {
            mVertices.put(i, mVertices.get(i) * scale);
        }
        mNormals.put(normalData).position(0);
        mIndices.put(indices).position(0);
        return mNumIndices;
    }

    ///
    //  Load indices from raw resource
    //
    private short[] loadIndices ( InputStream is ) {
        Scanner scanner = new Scanner(is);
        ArrayList<Integer> newList = new ArrayList<>();

        while (scanner.hasNext()) {
            newList.add(Integer.parseInt(scanner.next()));
        }

        short[] arr = new short[newList.size()];

        for (int i = 0; i < newList.size(); i++) {
            Integer entry;
            entry = newList.get(i);
            arr[i] = entry.shortValue();
        }
        return (arr);
    }

    ///
    //  Load float array from raw resource
    //
    private float[] loadFloatArray( InputStream is ) {
        Scanner scanner = new Scanner(is);
        ArrayList<Float> newList = new ArrayList<>();

        while (scanner.hasNext()) {
            newList.add(Float.parseFloat(scanner.next()));
        }

        float[] arr = new float[newList.size()];

        for (int i = 0; i < newList.size(); i++) {
            arr[i] = newList.get(i);
        }
        return (arr);
    }

    public FloatBuffer getVertices() {
        return mVertices;
    }

    public FloatBuffer getNormals() {
        return mNormals;
    }

    public FloatBuffer getTexCoords() {
        return mTexCoords;
    }

    public ShortBuffer getIndices() {
        return mIndices;
    }

    public int getNumIndices() {
        return mNumIndices;
    }
}
