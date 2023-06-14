# 构建镜像
docker buildx build --platform linux/amd64,linux/arm64 -t ziqix/ml-platform:flask-0.2.0 . --push
# 拉取镜像
docker pull ziqix/ml-platform:flask-0.2.0