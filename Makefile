target/tuneblendr-algorithms-0.1.0.jar:Dockerfile
	docker build -t shadash/neo4j-algo-tuneblendr:latest .
	docker run --cidfile build.cid shadash/neo4j-algo-tuneblendr:latest
	docker cp $$(cat ./build.cid):/project/target .
	rm ./build.cid
