package com.example.hellojni;

public class LibFuns {
	static {  
        System.loadLibrary("ImgProcess");  
    }
	
	public static native void ImgToGray(int[] buf, int w, int h);
	public static native void GuassianBlur(int[] bug, int w, int h);
}