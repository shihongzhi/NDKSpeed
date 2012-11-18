/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.hellojni;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


public class HelloJni extends Activity
{
	Button btnOriginal, btnJAVA, btnNDK, btnguassianJAVA, btnguassianNDK;
	ImageView imgView;
	Bitmap icon;
	int width, hight;
	int[] pixel;	//���ͼƬ����ֵ
	int[] resultpixel;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.setTitle("NDK��JAVA���ܱȽ�");
        icon=((BitmapDrawable) getResources().getDrawable(R.drawable.icon)).getBitmap();
        width =icon.getWidth();
        hight = icon.getHeight();  
        pixel = new int[width * hight];
        resultpixel = new int[width * hight];
        icon.getPixels(pixel, 0, width, 0, 0, width, hight);
        imgView = (ImageView)this.findViewById(R.id.ImageView01);
        imgView.setImageBitmap(icon);
        
        btnOriginal = (Button)this.findViewById(R.id.btnOriginal);
        btnOriginal.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
        		imgView.setImageBitmap(icon);
        	}
        });
        
        //JAVA �Ҷ�ͼת��
        btnJAVA = (Button)this.findViewById(R.id.btnJAVA);
        btnJAVA.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	long current=System.currentTimeMillis();
            	for(int i=0; i<100; i++){
            		ConvertGrayImg();
            	}
                long performance=System.currentTimeMillis()-current;
                Bitmap result=Bitmap.createBitmap(width, hight, Config.RGB_565);  
                result.setPixels(resultpixel, 0, width, 0, 0,width, hight);  
                //��ʾ�Ҷ�ͼ  
                imgView.setImageBitmap(result);  
                HelloJni.this.setTitle("w:"+String.valueOf(width)+",h:"+String.valueOf(hight)  
                        +"�Ҷ�ͼת��JAVA��ʱ "+String.valueOf(performance/100.0f)+" ����"); 
            }
          });
        
        //NDK �Ҷ�ͼת��
        btnNDK = (Button)this.findViewById(R.id.btnNDK);
        btnNDK.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	for(int i=0; i<hight*width; i++)
            	{
            		resultpixel[i] = pixel[i];
            	}
            	long current=System.currentTimeMillis();  
                //ͨ��ImgToGray.so�Ѳ�ɫ����תΪ�Ҷ�����        		
            	for(int i=0; i<100; i++){
            		LibFuns.ImgToGray(resultpixel, width, hight);  
            	}
                long performance=System.currentTimeMillis()-current;  
                Bitmap resultImg=Bitmap.createBitmap(width, hight, Config.RGB_565);  
        		resultImg.setPixels(resultpixel, 0, width, 0, 0,width, hight);
                //��ʾ�Ҷ�ͼ  
                imgView.setImageBitmap(resultImg);  
                HelloJni.this.setTitle("w:"+String.valueOf(width)+",h:"+String.valueOf(hight)  
                        +"�Ҷ�ͼת�� NDK��ʱ "+String.valueOf(performance/100.0f)+" ����"); 
            }
          });
        
        //��˹ģ��  5*5�ĸ�˹��
        //  2  4  5  4  2
        //  4  9  12 9  4
        //  5  12 15 12 5
        //  4  9  12 9  4
        //  2  4  5  4  2
        //����ϵ��Ϊ  1/115
        btnguassianJAVA = (Button)this.findViewById(R.id.btnGaussianJAVA);
        btnguassianJAVA.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
        		for(int i=0; i<hight*width; i++)
            	{
            		resultpixel[i] = pixel[i];
            	}
        		long current=System.currentTimeMillis();
            	for(int i=0; i<20; i++){
            		GuassianBlur();
            	}
                long performance=System.currentTimeMillis()-current;
                Bitmap result=Bitmap.createBitmap(width, hight, Config.RGB_565);  
                result.setPixels(resultpixel, 0, width, 0, 0,width, hight);  
                //��ʾ��˹ģ���Ľ��
                imgView.setImageBitmap(result);  
                HelloJni.this.setTitle("w:"+String.valueOf(width)+",h:"+String.valueOf(hight)  
                        +"��˹ģ�� JAVA��ʱ "+String.valueOf(performance/100.0f)+" ����");
        	}
        });
        
        btnguassianNDK = (Button)this.findViewById(R.id.btnGaussianNDK);
        btnguassianNDK.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
        		for(int i=0; i<hight*width; i++)
            	{
            		resultpixel[i] = pixel[i];
            	}
            	long current=System.currentTimeMillis();  
                //ͨ��ImgToGray.so����        		
            	for(int i=0; i<20; i++){
            		LibFuns.GuassianBlur(resultpixel, width, hight);  
            	}
                long performance=System.currentTimeMillis()-current;  
                Bitmap resultImg=Bitmap.createBitmap(width, hight, Config.RGB_565);  
        		resultImg.setPixels(resultpixel, 0, width, 0, 0,width, hight);
        		//��ʾ��˹ģ���Ľ�� 
                imgView.setImageBitmap(resultImg);  
                HelloJni.this.setTitle("w:"+String.valueOf(width)+",h:"+String.valueOf(hight)  
                        +"��˹ģ�� NDK��ʱ "+String.valueOf(performance/100.0f)+" ����"); 
        	}
        });
    }

    /** 
     * ����ԴͼƬתΪ�Ҷ�ͼ 
     * @param resID ��ԴID 
     * @return 
     */  
    public void ConvertGrayImg()  
    {   
        //color�ڴ����ṹΪ0xFF red green blue ;�ܹ�32λ��red��green��blue��ռ8λ
        int alpha=0xFF<<24;  
        for (int i = 0; i < hight; i++) {    
            for (int j = 0; j < width; j++) {    
                // ������ص���ɫ    
                int color = pixel[width * i + j];    
                int red = ((color & 0x00FF0000) >> 16);    
                int green = ((color & 0x0000FF00) >> 8);    
                int blue = color & 0x000000FF;    
                color = (red + green + blue)/3;    
                color = alpha | (color << 16) | (color << 8) | color;    
                resultpixel[width * i + j] = color;  
            }  
        }
    }
    
    public void GuassianBlur()
    {
    	int[] color = new int[25];
    	int wight[]= {2, 4, 5, 4, 2, 
    				4, 9, 12, 9, 4, 
    				5, 12, 15, 12, 5, 
    				4, 9, 12, 9, 4, 
    				2, 4, 5, 4, 2};
    	int red, green, blue;
    	int alpha=0xFF<<24; 
    	for(int i=2; i < hight - 2; i++)
    	{
    		for(int j=2; j < width - 2; j++)
    		{
    			for(int k=-2; k<=2; k++)
    			{
    				for(int t=-2; t<=2; t++)
    				{
    					color[(k+2)*5 + t + 2] = resultpixel[width * (i+k) + j + t];
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
    			resultpixel[width * i + j] = alpha | (red << 16) | (green << 8) | blue;
    		}
    	}
    }
}
