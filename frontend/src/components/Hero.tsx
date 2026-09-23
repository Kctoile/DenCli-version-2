import React from 'react';
import { ArrowRight, Phone } from 'lucide-react';
import { DENTAL_IMAGES } from '../data/dentalData';

interface HeroProps {
  onOpenBooking: () => void;
}

export const Hero: React.FC<HeroProps> = ({ onOpenBooking }) => {
  const serviceTags = [
    'Dental Checkup',
    'Teeth Cleaning',
    'Tooth Whitening',
    'Gum Treatment',
    'Implants',
    'Root Canal'
  ];

  const words = ['Experience', 'Comfortable', 'Dental', 'Care.'];

  return (
    <section className="relative min-h-[92vh] flex items-end overflow-hidden">
      {/* Background Image */}
      <img
        src={DENTAL_IMAGES.hero}
        alt="Modern dental clinic patient care"
        className="absolute inset-0 w-full h-full object-cover object-center z-0"
      />

      {/* Layer 1: Left-to-right directional fade */}
      <div
        className="absolute inset-0 z-[1] pointer-events-none"
        style={{
          background:
            'linear-gradient(to right, rgba(6,12,20,0.95) 0%, rgba(6,12,20,0.85) 38%, rgba(6,12,20,0.55) 65%, rgba(6,12,20,0.20) 100%)'
        }}
      />

      {/* Layer 2: Bottom fade into next section */}
      <div
        className="absolute inset-0 z-[2] pointer-events-none"
        style={{
          background: 'linear-gradient(to top, #060C14 0%, transparent 22%)'
        }}
      />

      {/* Content Container */}
      <div className="relative z-10 max-w-[1200px] mx-auto px-6 lg:px-[60px] w-full pb-16 pt-36">
        {/* Badge Pill */}
        <div className="inline-flex items-center gap-2 bg-[#0BB8B8]/15 border border-[#0BB8B8]/30 rounded-full px-4 py-1.5 mb-7 backdrop-blur-sm animate-fade-in">
          <span className="w-1.5 h-1.5 rounded-full bg-[#0BB8B8] animate-pulse" />
          <span className="font-semibold text-[0.62rem] tracking-[0.14em] text-[#0BB8B8] uppercase">
            BEST DENTAL CARE · TP. HỒ CHÍ MINH & HÀ NỘI
          </span>
        </div>

        {/* H1 Headline */}
        <h1 className="font-extrabold text-[clamp(2.8rem,5.5vw,5.5rem)] leading-[1.05] tracking-[-0.03em] text-white max-w-[620px] mb-6">
          {words.map((word, index) => {
            const isTeal = word === 'Dental' || word === 'Care.';
            return (
              <span
                key={index}
                className={`inline-block mr-3 transition-transform duration-500 ${
                  isTeal ? 'text-[#0BB8B8]' : 'text-white'
                }`}
              >
                {word}
              </span>
            );
          })}
        </h1>

        {/* Subline */}
        <p className="font-light text-[0.92rem] text-white/60 leading-[1.75] max-w-[420px] mb-10">
          Your family&apos;s dental health, handled with care. Modern technology, gentle hands, and
          transparent pricing — always.
        </p>

        {/* CTA Row */}
        <div className="flex flex-wrap gap-4 items-center mb-12">
          <button
            onClick={onOpenBooking}
            className="inline-flex items-center justify-center gap-2 shrink-0 whitespace-nowrap h-12 px-8 rounded-full text-[0.82rem] font-bold text-white bg-[#0BB8B8] hover:bg-[#099E9E] shadow-xl shadow-[#0BB8B8]/25 transition-all duration-200 active:scale-[0.98] group"
          >
            <span>Book Appointment</span>
            <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
          </button>

          <a
            href="tel:19006868"
            className="inline-flex items-center gap-2 text-[0.82rem] text-white/60 hover:text-white transition-colors px-2 py-2"
          >
            <Phone className="w-3.5 h-3.5 text-[#0BB8B8]" />
            <span>Or Call: <strong className="text-white/90 font-semibold">1900 6868</strong></span>
          </a>
        </div>

        {/* Service Tag Pills */}
        <div className="flex flex-wrap gap-2.5">
          {serviceTags.map((tag, idx) => (
            <span
              key={idx}
              className="bg-white/10 border border-white/15 rounded-full px-4 py-2 text-[0.72rem] font-medium text-white/75 backdrop-blur-sm hover:border-[#0BB8B8]/50 hover:bg-white/15 transition-all cursor-default"
            >
              {tag}
            </span>
          ))}
        </div>
      </div>
    </section>
  );
};
