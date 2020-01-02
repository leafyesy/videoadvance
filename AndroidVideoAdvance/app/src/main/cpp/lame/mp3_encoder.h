class Mp3Encoder {
    private:
        FILE* pcmFile;
        FILE* mp3File;
        lame_t lameClient;

        public:
            Mp3Encoder();
            ~Mp3Encoder();
            int Init(const char* pcmFilePath,const char *mp3FilePath,int sampleRate,int channels,int bitRate);
            void Encoder();
            void Destroy();
}