import React, { useState } from 'react';
import { Check } from 'lucide-react';

export const ProgressBar: React.FC = () => {
  const [activeStep, setActiveStep] = useState(0);

  const steps = [
    { title: 'Smile Assessment', sub: 'Digital 3D Diagnostics' },
    { title: 'Care Planning', sub: 'Transparent Options' },
    { title: 'Treatment Process', sub: 'Gentle & Painless' },
    { title: 'Dental Maintenance', sub: 'Long-term Smile Care' }
  ];

  return (
    <section className="bg-white border-b border-[#DDE4EE] select-none">
      <div className="max-w-[1200px] mx-auto px-6 lg:px-[60px]">
        <div className="grid grid-cols-2 md:grid-cols-4">
          {steps.map((step, idx) => {
            const isActive = idx === activeStep;
            const isCompleted = idx < activeStep;

            return (
              <button
                key={idx}
                onClick={() => setActiveStep(idx)}
                className={`text-left py-5 px-4 sm:px-6 border-r border-[#DDE4EE] last:border-r-0 relative transition-all duration-200 group focus:outline-none ${
                  isActive ? 'bg-slate-50/60' : 'hover:bg-slate-50/30'
                }`}
              >
                {/* Active indicator bar */}
                {isActive && (
                  <div className="absolute bottom-0 left-0 right-0 h-[2.5px] bg-[#0BB8B8] transition-all duration-300" />
                )}

                <div className="flex items-center gap-3">
                  <div
                    className={`w-6 h-6 rounded-full flex items-center justify-center text-[0.65rem] font-bold transition-all ${
                      isActive
                        ? 'bg-[#0BB8B8] text-white'
                        : isCompleted
                        ? 'bg-[#10B981] text-white'
                        : 'bg-slate-100 text-[#7A8BA0] group-hover:bg-slate-200'
                    }`}
                  >
                    {isCompleted ? <Check className="w-3.5 h-3.5" /> : `0${idx + 1}`}
                  </div>

                  <div className="flex flex-col">
                    <span
                      className={`text-[0.75rem] sm:text-[0.8rem] tracking-[0.02em] transition-colors ${
                        isActive
                          ? 'font-bold text-slate-900'
                          : 'font-medium text-[#7A8BA0] group-hover:text-slate-700'
                      }`}
                    >
                      {step.title}
                    </span>
                    <span className="text-[0.65rem] text-[#7A8BA0]/80 hidden sm:block">
                      {step.sub}
                    </span>
                  </div>
                </div>
              </button>
            );
          })}
        </div>
      </div>
    </section>
  );
};
