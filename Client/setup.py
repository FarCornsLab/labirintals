from setuptools import setup, find_packages
from os.path import join, dirname
from setuptools import Command
import os
class build_exe(Command):
    description = "run a custom build_exe command"
    user_options = [
      ('distpath=', None, 'path to out executable file'),
  ]

    def run(self):
        import PyInstaller.__main__
        import shutil
        if os.path.isdir('build'):
            shutil.rmtree('build')
        if os.path.isdir(self.distpath):
            shutil.rmtree(self.distpath)
            print("del "+self.distpath)

        PyInstaller.__main__.run([
            'main.py',
            '--windowed',
            '--distpath='+self.distpath,
            '--name=labirintals_client',
            '--add-data='+os.path.join('configs')+os.pathsep+'configs',
            '--add-data=resource'+os.pathsep+'resource'
        ])

    def initialize_options(self):
        self.distpath = 'dist'

    def finalize_options(self):
        pass

setup(
    name='labirintals_client',
    version='1.0',
    cmdclass={
        'build_exe': build_exe,
    },
    packages=find_packages(),
    long_description=open(join(dirname(__file__), 'README.md')).read(),
    install_requires=[
            'pyinstaller==4.1',
            'pygame==2.0.0',
            'pygame-gui==0.5.7'
        ]
)