#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
extern "C" {
JNIEXPORT void JNICALL Java_com_example_hellojni_LibFuns_ImgToGray(
		JNIEnv * env, jobject obj, jintArray buf, int w, int h);
JNIEXPORT void JNICALL Java_com_example_hellojni_LibFuns_GuassianBlur(
		JNIEnv * env, jobject obj, jintArray buf, int w, int h);
}
;


JNIEXPORT void JNICALL Java_com_example_hellojni_LibFuns_ImgToGray(
		JNIEnv * env, jobject obj, jintArray buf, int w, int h) {
    jint *cbuf;
    jboolean t = false;
    cbuf = env->GetIntArrayElements(buf, &t);
    if (cbuf == NULL) {
        return; /* exception occurred */
    }
    int alpha = 0xFF << 24;
    for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
            // 获得像素的颜色
            int color = cbuf[w * i + j];
            int red = ((color & 0x00FF0000) >> 16);
            int green = ((color & 0x0000FF00) >> 8);
            int blue = color & 0x000000FF;
            color = (red + green + blue) / 3;
            color = alpha | (color << 16) | (color << 8) | color;
            cbuf[w * i + j] = color;
        }
    }
}

JNIEXPORT void JNICALL Java_com_example_hellojni_LibFuns_GuassianBlur(
		JNIEnv * env, jobject obj, jintArray buf, int w, int h) {
	jint *cbuf;
	jboolean t = false;
	cbuf = env->GetIntArrayElements(buf, &t);
	if (cbuf == NULL) {
	    return; /* exception occurred */
	}
	int color[25];
	int wight[]= {2, 4, 5, 4, 2,
	    		4, 9, 12, 9, 4,
	    		5, 12, 15, 12, 5,
	    		4, 9, 12, 9, 4,
	    		2, 4, 5, 4, 2};
	int red, green, blue;
	int alpha=0xFF<<24;
	for(int i=2; i < h - 2; i++)
	{
	    for(int j=2; j < w - 2; j++)
	    {
	    	for(int k=-2; k<=2; k++)
	    	{
	    		for(int t=-2; t<=2; t++)
	    		{
	    			color[(k+2)*5 + t + 2] = cbuf[w * (i+k) + j + t];
	    		}
	    	}
	    	red = 0;
	    	green = 0;
	    	blue = 0;
	    	for(int k=0; k<25; k++)
	    	{
	    		red += ((color[k] & 0x00FF0000) >> 16) * wight[k];
	    		green += ((color[k] & 0x0000FF00) >> 8) * wight[k];
	    		blue += (color[k] & 0x000000FF) * wight[k];
	    	}

	    	red /= 159;
	    	green /= 159;
	    	blue /= 159;
	    	red = red > 255 ? 255 : red;
	    	green = green > 255 ? 255 : green;
	    	blue = blue > 255 ? 255 : blue;
	    	cbuf[w * i + j] = alpha | (red << 16) | (green << 8) | blue;
	    }
	}
}
