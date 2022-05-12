FROM gitpod/workspace-full-vnc
RUN sudo apt-get update && \
    sudo apt-get install -y libgtk-3-dev python-tk python3-tk tk-dev && \
    sudo rm -rf /var/lib/apt/lists/*
