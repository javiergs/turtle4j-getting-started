FROM ubuntu:22.04

ENV LANG=en_US.UTF-8
ENV DEBIAN_FRONTEND=noninteractive
ENV ROS_DISTRO=humble

# Set up locale
RUN apt update && apt install -y locales && \
    locale-gen en_US en_US.UTF-8 && \
    update-locale LANG=en_US.UTF-8 LC_ALL=en_US.UTF-8

# Install essential utilities and LXDE desktop with better terminal support
RUN apt update && apt install -y \
    wget curl gnupg2 lsb-release sudo software-properties-common \
    python3-pip git \
    lxsession lxde-core lxde-common lxterminal xfce4-terminal xterm \
    x11vnc xvfb supervisor novnc websockify netsurf-gtk

# Provide a working lightweight browser for LXDE's default web-browser panel launcher.
RUN printf '[Desktop Entry]\nType=Application\nName=Web Browser\nExec=netsurf-gtk %%u\nIcon=web-browser\nTerminal=false\nCategories=Network;WebBrowser;\n' \
    > /usr/share/applications/lxde-x-www-browser.desktop

# Use a single LXDE/Openbox workspace rather than the default four.
RUN mkdir -p /root/.config/openbox && \
    cp /etc/xdg/openbox/rc.xml /root/.config/openbox/lxde-rc.xml && \
    sed -i '/<desktops>/,/<\/desktops>/s|<number>[0-9]*</number>|<number>1</number>|' /root/.config/openbox/lxde-rc.xml

# Disable LXDE's PolicyKit authentication agent in this container-only desktop.
# There is no logind user session, so lxpolkit otherwise shows "No session for pid".
RUN if [ -f /etc/xdg/lxsession/LXDE/desktop.conf ]; then \
      sed -i 's|^polkit/command=.*|polkit/command=|' /etc/xdg/lxsession/LXDE/desktop.conf; \
    fi && \
    rm -f /etc/xdg/autostart/lxpolkit.desktop && \
    if [ -f /usr/bin/lxpolkit ]; then chmod a-x /usr/bin/lxpolkit; fi

# Install ROS tools using pip (compatible with ARM64)
RUN pip3 install -U colcon-common-extensions rosdep

# Add ROS 2 Humble sources
RUN curl -sSL https://raw.githubusercontent.com/ros/rosdistro/master/ros.key | apt-key add - && \
    echo "deb http://packages.ros.org/ros2/ubuntu $(lsb_release -cs) main" > /etc/apt/sources.list.d/ros2-latest.list

# Install ROS and TurtleBot-related packages
RUN apt update && apt install -y \
    ros-humble-ros-base \
    ros-humble-turtlesim \
    ros-humble-rosbridge-server \
    ros-humble-teleop-twist-keyboard \
    ros-dev-tools

# Initialize rosdep
RUN mkdir -p /etc/ros/rosdep/sources.list.d && rosdep init && rosdep update

RUN echo "source /opt/ros/humble/setup.bash" >> ~/.bashrc

# Copy supervisord configuration
COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

# Expose ports for VNC, noVNC and rosbridge
EXPOSE 5900 6080 9090

# Start everything with supervisord
CMD ["/usr/bin/supervisord"]

# Create Desktop folder
RUN mkdir -p /root/Desktop
# Create 2D simulator desktop shortcut
RUN echo '[Desktop Entry]\n\
Name=Turtlesim 2D\n\
Exec=lxterminal -e "bash -c '\''source /opt/ros/humble/setup.bash && ros2 run turtlesim turtlesim_node'\''"\n\
Icon=utilities-terminal\n\
Terminal=false\n\
Type=Application\n\
Categories=Application;' > /root/Desktop/start_2d_sim.desktop && \
    chmod +x /root/Desktop/start_2d_sim.desktop

# Create teleop control desktop shortcut
RUN echo '[Desktop Entry]\n\
Name=Keyboard Teleop 2D\n\
Exec=lxterminal -e "bash -c '\''source /opt/ros/humble/setup.bash && ros2 run teleop_twist_keyboard teleop_twist_keyboard --ros-args -r /cmd_vel:=/turtle1/cmd_vel'\''"\n\
Icon=utilities-terminal\n\
Terminal=false\n\
Type=Application\n\
Categories=Application;' > /root/Desktop/teleop_keyboard.desktop && \
    chmod +x /root/Desktop/teleop_keyboard.desktop

# Use the existing TURTLE4J wallpaper as the LXDE desktop background.
COPY turtle4j-wallpaper.png /usr/share/backgrounds/turtle4j-wallpaper.png
RUN mkdir -p /root/.config/pcmanfm/LXDE && \
    printf '[*]\nwallpaper_mode=stretch\nwallpaper=/usr/share/backgrounds/turtle4j-wallpaper.png\ndesktop_fg=#000000\n' \
    > /root/.config/pcmanfm/LXDE/desktop-items-0.conf
