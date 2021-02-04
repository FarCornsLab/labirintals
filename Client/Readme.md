Before start you need to **Install Python 3**

### building executable file:
```
git clone https://github.com/FarCornsLab/labirintals
cd ./labirintals/Client
py setup.py install
py setup.py build_exe
``` 
now you can run game

Windows:
```
.\dist\labirintals_client\labirintals_client.exe
```

Unix:
```
./dist/labirintals_client/labirintals_client
```

### Observer mode
If you want watch a game like observer, you need to specify observer token, same as used in server config in "OBSERVER_TOKEN" field.  
