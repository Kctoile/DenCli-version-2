import React from 'react';
import { Phone, ArrowRight, Check } from 'lucide-react';

interface FinalCtaProps {
  onOpenBooking: () => void;
}

export const FinalCta: React.FC<FinalCtaProps> = ({ onOpenBooking }) => {
  return (
    <section className="bg-[#0BB8B8] py-20 px-6 lg:px-[60px] text-white relative overflow-hidden">
      {/* Decorative subtle circles */}
      <div className="absolute top-0 right-0 w-96 h-96 bg-white/5 rounded-full blur-3xl pointer-events-none -mr-20 -mt-20" />
      <div className="absolute bottom-0 left-0 w-96 h-96 bg-black/5 rounded-full blur-3xl pointer-events-none -ml-20 -mb-20" />

      <div className="max-w-2xl mx-auto text-center relative z-10">
        <h2 className="font-extrabold text-[clamp(2rem,4vw,3.5rem)] tracking-[-0.03em] text-white leading-tight">
          Your healthiest smile starts today.
        </h2>

        <p className="font-light text-[0.92rem] text-white/85 mt-5 leading-relaxed">
          Request a free consultation in under 2 minutes. Same-day appointments available.
          Transparent pricing, no surprises.
        </p>

        {/* CTA Buttons */}
        <div className="flex gap-4 justify-center flex-wrap mt-10">
          <button
            onClick={onOpenBooking}
            className="inline-flex items-center justify-center gap-2 shrink-0 whitespace-nowrap h-12 px-10 rounded-full text-[0.85rem] font-extrabold text-[#0BB8B8] bg-white hover:bg-white/95 shadow-2xl transition-all active:scale-[0.98] group"
          >
            <span>Book Free Consultation</span>
            <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
          </button>

          <a
            href="tel:19006868"
            className="inline-flex items-center justify-center gap-2 shrink-0 whitespace-nowrap h-12 px-8 rounded-full text-[0.85rem] font-medium text-white border border-white/40 hover:bg-white/10 transition-colors"
          >
            <Phone className="w-4 h-4" />
            <span>1900 6868</span>
          </a>
        </div>

        {/* Trust Line */}
        <div className="flex justify-center gap-6 sm:gap-8 flex-wrap mt-9 text-[0.8rem] text-white/80 font-medium">
          <span className="flex items-center gap-1.5">
            <Check className="w-4 h-4 text-white" /> No waiting list
          </span>
          <span className="flex items-center gap-1.5">
            <Check className="w-4 h-4 text-white" /> Transparent pricing
          </span>
          <span className="flex items-center gap-1.5">
            <Check className="w-4 h-4 text-white" /> All ages welcome
          </span>
        </div>
      </div>
    </section>
  );
};
