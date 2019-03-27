import random
import os
import time

def createTestFile():
    if not os.path.exists("./Testfiles/"):
        os.mkdir("./Testfiles")
    os.system("base64 /dev/urandom | head -c 10000000 > ./Testfiles/Testfile00.txt")
    time.sleep(5)
    f = open("./Testfiles/Testfile00.txt")
    text = f.read()
    for i in range(1,50):
        modified = text
        for j in range(5):
            index = random.randint(0,9999999)
            modified = modified[:index]+str(random.randint(0,9))+modified[index+1:]
        path = "./Testfiles/Testfile"+str(i).zfill(2)+".txt"
        w = open(path,'w')
        w.write(modified)
        w.close()
    f.close()
