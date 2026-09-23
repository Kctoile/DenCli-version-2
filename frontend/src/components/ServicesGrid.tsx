import React from 'react';
import { ArrowRight } from 'lucide-react';
import { DENTAL_IMAGES } from '../data/dentalData';

interface ServicesGridProps {
  onSelectService: (serviceTitle: string) => void;
}

export const ServicesGrid: React.FC<ServicesGridProps> = ({ onSelectService }) => {
  return (
    <section id="services" className="bg-[#EEF2F7] py-24 px-6 lg:px-[60px]">
      <div className="max-w-[1200px] mx-auto">
        {/* Header Row */}
        <div className="flex flex-col md:flex-row justify-between items-start md:items-end gap-6 mb-14">
          <div>
            <span className="block text-[0.65rem] tracking-[0.2em] font-bold text-[#0BB8B8] uppercase mb-3">
              FEATURE TREATMENT
            </span>
            <h2 className="font-extrabold text-[clamp(1.8rem,3vw,2.8rem)] tracking-[-0.025em] leading-[1.2] text-[#0A1520]">
              Advanced Dental Care <br className="hidden sm:block" />
              for a Healthier Smile
            </h2>
          </div>

          <button
            onClick={() => onSelectService('All Services')}
            className="inline-flex items-center justify-center gap-2 shrink-0 whitespace-nowrap h-10 px-6 rounded-full text-[0.78rem] font-bold text-white bg-[#0A1520] hover:bg-[#1A2838] transition-all active:scale-[0.98]"
          >
            <span>View All Services</span>
            <ArrowRight className="w-3.5 h-3.5 text-[#0BB8B8]" />
          </button>
        </div>

        {/* 6 Cards Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {DENTAL_IMAGES.services.map((service) => (
            <div
              key={service.id}
              onClick={() => onSelectService(service.title)}
              className="bg-white rounded-2xl overflow-hidden border border-[#DDE4EE] cursor-pointer group transition-all duration-200 hover:-translate-y-[5px] hover:shadow-[0_20px_56px_rgba(0,0,0,0.09)] flex flex-col"
            >
              {/* Photo Container */}
              <div className="h-[160px] overflow-hidden relative bg-slate-100">
                <img
                  src={service.image}
                  alt={service.title}
                  className="w-full h-full object-cover transition-transform duration-300 ease-out group-hover:scale-105"
                />
                <div className="absolute inset-0 bg-gradient-to-t from-black/20 via-transparent to-transparent pointer-events-none" />
              </div>

              {/* Content */}
              <div className="p-6 flex flex-col flex-1 justify-between">
                <div>
                  <span className="inline-flex bg-[#EBF9F9] text-[#0BB8B8] text-[0.65rem] font-bold tracking-[0.08em] px-3 py-1 rounded-full mb-3">
                    {service.chip}
                  </span>

                  <h3 className="font-bold text-[1rem] text-[#0A1520] mb-2 group-hover:text-[#0BB8B8] transition-colors">
                    {service.title}
                  </h3>

                  <p className="font-light text-[0.8rem] text-[#7A8BA0] leading-[1.65]">
                    {service.description}
                  </p>
                </div>

                <div className="mt-5 pt-3 border-t border-slate-100 flex items-center justify-between">
                  <span className="text-[0.78rem] font-semibold text-[#0BB8B8] inline-flex items-center gap-1 group-hover:gap-2 transition-all">
                    Book Treatment <ArrowRight className="w-3.5 h-3.5" />
                  </span>
                  <span className="text-[0.7rem] text-[#7A8BA0]/70 font-medium">
                    FDA Approved
                  </span>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};
