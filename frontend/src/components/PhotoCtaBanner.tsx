import React from 'react';
import { Phone, ArrowRight, Sparkles } from 'lucide-react';
import { DENTAL_IMAGES } from '../data/dentalData';

interface PhotoCtaBannerProps {
  onOpenBooking: () => void;
}

export const PhotoCtaBanner: React.FC<PhotoCtaBannerProps> = ({ onOpenBooking }) => {
  return (
    <section className="relative h-[340px] overflow-hidden flex items-center">
      {/* Background Image */}
      <img
        src={DENTAL_IMAGES.ctaBanner}
        alt="Confident dental smile"
        className="absolute inset-0 w-full h-full object-cover object-center z-0"
      />

      {/* Directional Overlay */}
      <div
        className="absolute inset-0 z-[1]"
        style={{
          background:
            'linear-gradient(to right, rgba(6,12,20,0.95) 0%, rgba(6,12,20,0.80) 45%, rgba(6,12,20,0.30) 100%)'
        }}
      />

      {/* Inner Centered Content */}
      <div className="relative z-10 max-w-[1200px] mx-auto px-6 lg:px-[60px] w-full flex flex-col md:flex-row justify-between items-start md:items-center gap-6">
        <div>
          <div className="inline-flex items-center gap-1.5 text-[#0BB8B8] text-[0.72rem] font-bold tracking-widest uppercase mb-2">
            <Sparkles className="w-3.5 h-3.5" />
            <span>CONFIDENT TEETH TRANSFORMATION</span>
          </div>
          <h2 className="font-extrabold text-[clamp(1.8rem,3.5vw,3rem)] tracking-[-0.025em] text-white leading-tight">
            Ready for your best smile?
          </h2>
          <p className="font-light text-[0.88rem] text-white/70 mt-2">
            Same-day appointments available. No waiting lists. 100% painless guarantee.
          </p>
        </div>

        {/* Buttons */}
        <div className="flex items-center gap-3.5 flex-wrap">
          <button
            onClick={onOpenBooking}
            className="inline-flex items-center justify-center gap-2 shrink-0 whitespace-nowrap h-12 px-8 rounded-full text-[0.82rem] font-bold text-white bg-[#0BB8B8] hover:bg-[#099E9E] shadow-xl shadow-[#0BB8B8]/30 transition-all active:scale-[0.98] group"
          >
            <span>Book Appointment</span>
            <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
          </button>

          <a
            href="tel:19006868"
            className="inline-flex items-center justify-center gap-2 shrink-0 whitespace-nowrap h-12 px-7 rounded-full text-[0.82rem] font-medium text-white bg-white/10 hover:bg-white/20 border border-white/20 backdrop-blur-sm transition-all active:scale-[0.98]"
          >
            <Phone className="w-4 h-4 text-[#0BB8B8]" />
            <span>1900 6868</span>
          </a>
        </div>
      </div>
    </section>
  );
};
