#install packages
sudo apt-get install gobjc
sudo apt-get install gnustep
sudo apt-get install gnustep-common
sudo apt-get install gnustep-devel
sudo apt-get install gnustep-make

#update packages
sudo apt-get update
sudo apt-get upgrade

#alias start gnustep
echo "alias gnustepstart='. /usr/share/GNUstep/Makefiles/GNUstep.sh'\n" >> /home/$USER/.bashrc
