import os
import subprocess

filename = "test0.mp4"

def main(filename):
        # get all the video file name
    task = ['ffmpeg',
            '-i',
            filename,
            '-r',
            '30',
            '-s',
            'hd480',
            '-b:v',
            '1024k',
            '-loglevel',
            'quiet',
            'test1.mp4',
            '-r',
            '30',
            '-s',
            'hd720',
            '-b:v',
            '2048k',
            '-loglevel',
            'quiet',
            'test2.mp4']
    subprocess.call(task)

    print("mission complete")

if __name__ == '__main__':
    main(filename)
