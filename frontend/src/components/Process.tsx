import React from 'react';
import { DENTAL_IMAGES } from '../data/dentalData';

export const Process: React.FC = () => {
  return (
    <section id="process" className="bg-[#0A1520] py-24 px-6 lg:px-[60px]">
      <div className="max-w-[1200px] mx-auto">
        {/* Header */}
        <div className="max-w-[560px] mx-auto text-center mb-16">
          <span className="text-[0.65rem] tracking-[0.2em] font-bold text-[#0BB8B8] uppercase">
            HOW IT WORKS
          </span>
          <h2 className="font-extrabold text-[clamp(1.8rem,3vw,2.8rem)] tracking-[-0.025em] text-white mt-3">
            Your journey to a healthier smile.
          </h2>
        </div>

        {/* 4 Steps Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
          {DENTAL_IMAGES.process.map((step, idx) => (
            <div
              key={idx}
              className="bg-white/[0.04] border border-white/[0.08] rounded-2xl p-6 relative hover:border-[#0BB8B8]/40 hover:bg-white/[0.06] transition-all duration-300 group flex flex-col justify-between"
            >
              {/* Giant faint step number */}
              <div className="font-extrabold text-[3.2rem] tracking-[-0.04em] text-white/[0.07] leading-[1] mb-4 select-none group-hover:text-white/[0.12] transition-colors">
                {step.number}
              </div>

              {/* Step photo */}
              <div className="h-[135px] rounded-xl overflow-hidden mb-5 bg-white/5 relative">
                <img
                  src={step.image}
                  alt={step.title}
                  className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                />
                <div className="absolute inset-0 bg-gradient-to-t from-[#0A1520]/60 via-transparent to-transparent pointer-events-none" />
              </div>

              {/* Title & Badge */}
              <div className="flex items-center gap-2.5 mb-3">
                <span className="w-6 h-6 rounded-full bg-[#0BB8B8] flex items-center justify-center text-white text-[0.65rem] font-bold shrink-0 shadow-sm">
                  {idx + 1}
                </span>
                <h3 className="font-bold text-[0.92rem] text-white group-hover:text-[#0BB8B8] transition-colors">
                  {step.title}
                </h3>
              </div>

              {/* Description */}
              <p className="font-light text-[0.78rem] text-white/55 leading-relaxed flex-1">
                {step.description}
              </p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};
