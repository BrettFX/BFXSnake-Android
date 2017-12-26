package io.github.brettfx.bfxsnake.Sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * @author brett
 * @since 12/24/2017
 */

public class Animation {
    private Array<TextureRegion> m_frames;
    private float m_maxFrameTime;
    private float m_currentFrameTime;
    private int m_frameCount;
    private int m_frame;


    public Animation(TextureRegion region, int frameCount, float cycleTime){
        m_frames = new Array<TextureRegion>();
        TextureRegion temp;
        int frameWidth = region.getRegionWidth() / frameCount;
        for(int i = 0; i < frameCount; i++){
            temp = new TextureRegion(region, i * frameWidth, 0, frameWidth, region.getRegionHeight());
            m_frames.add(temp);
        }

        m_frameCount = frameCount;
        m_maxFrameTime = cycleTime / frameCount;
        m_frame = 0;
    }

    public void update(float dt){
        m_currentFrameTime += dt;
        if(m_currentFrameTime > m_maxFrameTime){
            m_frame++;
            m_currentFrameTime = 0;
        }
        if(m_frame >= m_frameCount)
            m_frame = 0;

    }

    public void flip(){
        for(TextureRegion region : m_frames)
            region.flip(true, false);
    }

    public TextureRegion getFrame(){
        return m_frames.get(m_frame);
    }
}
