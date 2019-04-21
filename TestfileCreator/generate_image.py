import cv2 as cv
import os
import imutils

filename = 'test0.png'
path = os.getcwd()

def rotate(filename):
	
	
	curpath = path + '/rotate_images'
	os.mkdir(curpath)
	img = cv.imread(filename)
	for i in range(1,100):
		img_tmp = imutils.rotate(img,i)
		savepath = curpath+'/'+str(i)+'.png'
		cv.imwrite(savepath, img_tmp)

def resize(filename):
	curpath = path + '/resize_images'
	os.mkdir(curpath)
	img = cv.imread(filename)
	
	for i in range(1,100):
		scale_percent = 100 - i/2
		width = int(img.shape[1] * scale_percent / 100)
		height = int(img.shape[0] * scale_percent / 100)
		dim = (width, height)
		img_tmp = cv.resize(img,dim)
		savepath = curpath+'/'+str(i)+'.png'
		cv.imwrite(savepath, img_tmp)

rotate(filename)
resize(filename)