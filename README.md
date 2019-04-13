# Deduplicator

## How to use dedup command?
go to the `release` folder, there are two files: `dedup` and jar file `deduplicator.jar`.  
then use  
```
chmod +x dedup
```
make dedup executable.  


add the path of dedup into the `PATH` environment variable.  
```
export PATH=$PATH:/home/yuxuansu/study/EC504/Deduplicator/release
```

then you can use the dedup command to add file or retrieve file  
```
dedup -addFile <filename> -locker <lockername>
dedup -retrieveFile <filename> -output <file saved path> -locker <lockername>
```

all the chunks and metadata will be stored into `$HOME/.dedupStore` directory.