from setuptools import setup, find_packages
from os.path import join, dirname

setup(
    name='labirintals_client',
    version='1.0',
    packages=find_packages(),
    long_description=open(join(dirname(__file__), 'README.md')).read(),
    install_requires=[
            'pyinstaller==4.1',
            'pygame==2.0.0',
            'pygame-gui==0.5.7'
        ]
)