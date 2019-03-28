import subprocess
import os
import time
import shutil
import sys
import filecmp
from TestfileCreator import createTestFile

def getdirsize(dir):
   size = 0.
   for root, dirs, files in os.walk(dir):
      size += sum([os.path.getsize(os.path.join(root, name)) for name in files])
   return size

if __name__ == "__main__":
    # clear all test files
    if os.path.exists("./Testfiles"):
        shutil.rmtree("./Testfiles")
        os.mkdir("./Testfiles")
    if os.path.exists(os.getenv("HOME") + "/.dedupStore/chunks"):
        shutil.rmtree(os.getenv("HOME") + "/.dedupStore/chunks")
    if os.path.exists(os.getenv("HOME") + "/.dedupStore/lockers"):
        shutil.rmtree(os.getenv("HOME") + "/.dedupStore/lockers")
    
    # generate 50 10MB ACSII files
    createTestFile()

    # locker for test
    test_locker_name = "testLocker"

    # store all files into testLocker
    for i in range(50):
        file_name = "./Testfiles/Testfile"+str(i).zfill(2)+".txt"
        cmd = ["dedup", "-addFile", file_name, "-locker", test_locker_name]
        try:
            subprocess.check_call(cmd)
        except OSError:
            print("cmd dedup not found. please install ffmpeg in your computer first")
            sys.exit(-1)
        except subprocess.CalledProcessError as e:
            print("error when execute command, error msg: %s" % e)
            sys.exit(-1)
    
    # os.mkdir("./Testfiles/output")
    # retrieve all the files from testLocker
    for i in range(50):
        file_name = "Testfile"+str(i).zfill(2)+".txt"
        cmd = ["dedup", "-retrieveFile", file_name, "-output", '.', "-locker", test_locker_name]
        try:
            subprocess.check_call(cmd)
        except OSError:
            print("cmd dedup not found. please install ffmpeg in your computer first")
            sys.exit(-1)
        except subprocess.CalledProcessError as e:
            print("error when execute command, error msg: %s" % e)
            sys.exit(-1)
        ori_file_name = "./Testfiles/Testfile"+str(i).zfill(2)+".txt"

        if not filecmp.cmp(file_name, ori_file_name):
            print("error when compare files: {}".format(ori_file_name))
            sys.exit(-1)
        os.remove(file_name)

    # show disk usage statics
    test_files_size = getdirsize("./Testfiles")
    print("test file total size: %.3f MB" % (test_files_size/1024/1024))
    chunks_size = getdirsize(os.getenv("HOME") + "/.dedupStore/chunks")
    lockers_size = getdirsize(os.getenv("HOME") + "/.dedupStore/lockers")
    print("actual usage: %.3f MB" % ((chunks_size+lockers_size)/1024/1024))
    print("lockers: %.3f MB" % (lockers_size/1024/1024))
    print("chunks: %.3f MB" % (chunks_size/1024/1024))