//
// Created by leafye on 2020/3/20.
//

#ifndef VIDEOPLAYER_AVPACKET_QUEUE_H
#define VIDEOPLAYER_AVPACKET_QUEUE_H

#include <pthread.h>

/**
 * 音视频数据队列
 * 该数据队列存储了从音视频流中通过av_read_frame读取的AVPacket
 * 并维护两个index 分别表示写入和读取的位置
 */
typedef struct AVPacketQueue {
    //队列大小
    int size;
    //指针数组 **表示指针数组
    void **packets;
    //下一个写入的packet
    int next_to_write;
    //下一个读取的packet
    int next_to_read;
} AVPacketQueue;

AVPacketQueue *queue_init(int size);

void queue_free(AVPacketQueue *queue);

void *queue_push(AVPacketQueue *queue, pthread_mutex_t *mutex, pthread_cond_t *cond);

void *queue_pop(AVPacketQueue *queue, pthread_mutex_t *mutex, pthread_cond_t *cond);

#endif //VIDEOPLAYER_AVPACKET_QUEUE_H