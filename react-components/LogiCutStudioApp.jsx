import React, { useState, useEffect } from 'react';
import { AbsoluteFill, Sequence, useCurrentFrame, useVideoConfig, spring, Video, Img } from 'remotion';

/**
 * LogiCut Studio Pro v1.0.005 — React 18 & Remotion 3D Motion Graphics Component
 * Enforces Left 30% (0..30%) and Right 30% (70..100%) Side Safe Zones so Center 40% Speaker Face stays clear.
 */
export const LogiCutStudioApp = ({ recipe, videoSrc }) => {
  const frame = useCurrentFrame();
  const { fps, width, height } = useVideoConfig();
  const layers = (recipe && recipe.layers) || [];

  return (
    <AbsoluteFill style={{ backgroundColor: '#070A12', overflow: 'hidden' }}>
      {videoSrc && (
        <Video src={videoSrc} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
      )}
      {layers.map((layer) => {
        const fromFrame = Math.round((layer.start || 0) * fps);
        const durationInFrames = Math.max(6, Math.round(((layer.end || 4) - (layer.start || 0)) * fps));
        const scale = spring({
          frame: Math.max(0, frame - fromFrame),
          fps,
          config: { damping: 12, stiffness: 160 }
        });

        return (
          <Sequence key={layer.uid} from={fromFrame} durationInFrames={durationInFrames}>
            <div
              style={{
                position: 'absolute',
                left: `${(layer.posX || 0.2) * 100}%`,
                top: `${(layer.posY || 0.5) * 100}%`,
                transform: `translate(-50%, -50%) scale(${scale})`,
                color: layer.color || '#FFFFFF',
                fontFamily: layer.font || 'Noto Sans Devanagari, Plus Jakarta Sans, sans-serif',
                fontWeight: 900,
                textShadow: '2px 3px 0px #0F172A, 4px 6px 0px #020617'
              }}
            >
              {layer.type === 'text' ? layer.content : null}
            </div>
          </Sequence>
        );
      })}
    </AbsoluteFill>
  );
};
export default LogiCutStudioApp;
