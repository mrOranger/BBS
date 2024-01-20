FROM redis

ENV redis-password=eYVX7EwVmmxKPCDmwMtyKVge8oLd2t81

VOLUME redis-volume /data

EXPOSE 6379

CMD ["redis-server", "--requirepass ${redis-password}"]