# 로컬에서 AWS Redis를 접근하려면 어떻게 사용해야할까?

> AWS Redis 클러스터는 일반적으로 VPC(가상 사설 클라우드) 내부에 배치되어 있습니다. 
> VPC는 AWS 클라우드 내에서 논리적으로 격리된 네트워크 환경을 제공하며, 기본적으로 외부에서의 직접 접근을 허용하지 않습니다.

위 특징으로 로컬에서 Redis에 접속하기 위해서는 Bastion Host가 필요하다.

## 절차

1. AWS EC2 Bastion 생성
2. 로컬에 레디스를 설치한다.
3. 로컬에서 EC2 Port Forwarding (로컬:6379 -> Bastion EC2 -> Redis)
4. 

### 1. AWS EC2 Bastion 생성

### 2. 레디스를 설치한다.
```
# 로컬
brew install redis

# AWS EC2 Bastion 에서..
sudo yum update
sudo yum install gcc make 

mkdir Redis && cd Redis

# redis-cli 설치 및 make
wget http://download.redis.io/redis-stable.tar.gz && tar xvzf redis-stable.tar.gz && cd redis-stable && make

# redis-cli를 bin에 추가해 어느 위치서든 사용 가능하게 등록
sudo cp src/redis-cli /usr/bin/
```

### 3. 로컬에서 EC2 Port Forwarding (로컬:6379 -> Bastion EC2 -> Redis)
```
ssh -i [pem.key] -L 6379:[Redis EndPoint]:[Redis Port] [bastion user]@[bastion ec2 host] -p [bastion 접속 port]

# 로컬에서 포트포워딩  확인
lsof -i4 -P | grep -i "listen" | grep 6379
```

### 4. 접속확인
나의 경우, 전송 중 암호화 설정 ON, 클러스터 모드 활성화 상태.

[접속방법 - 참고 공식문서](https://docs.aws.amazon.com/ko_kr/AmazonElastiCache/latest/red-ug/GettingStarted.ConnectToCacheNode.html)

```
redis-cli -c -h localhost --tls
# 옵션 -c: AWS ElastiCache for Redis의 클러스터가 '클러스터 모드 비활성화'인 경우 생략 가능
# 옵션 -h: {redis-endpoint or host}부분이 localhost면 생략 가능
# 옵션 -p: 기본값 포트 6379면 생략 가능
# 옵션 --tls: AWS ElastiCache for Redis의 클러스터의 '전송 중 암호화'설정이 활성화일때 사용
```

연결된 모습 

![image](https://github.com/kakjzi/kakjzi/assets/82758364/96937fda-5f24-4d41-8d67-c1498c09a8b2)