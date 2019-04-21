import cv2 as cv
import os
import imutils
import time
import shutil
import sys
import filecmp
import subprocess

filename = 'test0.png'
path = os.getcwd()

def getdirsize(dir):
   size = 0.
   for root, dirs, files in os.walk(dir):
      size += sum([os.path.getsize(os.path.join(root, name)) for name in files])
   return size

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

if __name__ == "__main__":
	# rotate(filename)
	# resize(filename)

	# clear all test files
    if os.path.exists("./Testfiles"):
        shutil.rmtree("./Testfiles")
        os.mkdir("./Testfiles")
    if os.path.exists(os.getenv("HOME") + "/.dedupStore/chunks"):
        shutil.rmtree(os.getenv("HOME") + "/.dedupStore/chunks")
    if os.path.exists(os.getenv("HOME") + "/.dedupStore/lockers"):
        shutil.rmtree(os.getenv("HOME") + "/.dedupStore/lockers")

    # # # locker for test
    # test_locker_name = "testLocker"

    # # store all files into testLocker
    # file_name = "./rotate_images"
    # cmd = ["dedup", "-addFile", file_name, "-locker", test_locker_name]
    # try:
    #     subprocess.check_call(cmd)
    # except OSError:
    #     print("cmd dedup not found. please install ffmpeg in your computer first")
    #     sys.exit(-1)
    # except subprocess.CalledProcessError as e:
    #     print("error when execute command, error msg: %s" % e)
    #     sys.exit(-1)

    # file_name = "./resize_images"
    # cmd = ["dedup", "-addFile", file_name, "-locker", test_locker_name]
    # try:
    #     subprocess.check_call(cmd)
    # except OSError:
    #     print("cmd dedup not found. please install ffmpeg in your computer first")
    #     sys.exit(-1)
    # except subprocess.CalledProcessError as e:
    #     print("error when execute command, error msg: %s" % e)
    #     sys.exit(-1)

    # # show disk usage statics
    # test_files_size = getdirsize("./resize_images") + getdirsize("./rotate_images")
    # print("test file total size: %.3f MB" % (test_files_size/1024/1024))
    # chunks_size = getdirsize(os.getenv("HOME") + "/.dedupStore/chunks")
    # lockers_size = getdirsize(os.getenv("HOME") + "/.dedupStore/lockers")
    # print("actual usage: %.3f MB" % ((chunks_size+lockers_size)/1024/1024))
    # print("lockers: %.3f MB" % (lockers_size/1024/1024))
    # print("chunks: %.3f MB" % (chunks_size/1024/1024))