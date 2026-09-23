import React from 'react';
import { CheckCircle2, ArrowRight, Award, Shield } from 'lucide-react';
import { DENTAL_IMAGES } from '../data/dentalData';

interface WhyChooseUsProps {
  onOpenBooking: () => void;
}

export const WhyChooseUs: React.FC<WhyChooseUsProps> = ({ onOpenBooking }) => {
  const checklist = [
    'Dental check-ups & 3D scans',
    'Root canal microsurgery',
    'Airflow hygiene treatments',
    'Dental implant restoration',
    'Crowns, veneers & bridges',
    'Professional tooth whitening'
  ];

  return (
    <section className="bg-white py-24 px-6 lg:px-[60px]">
      <div className="max-w-[1200px] mx-auto">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-20 items-center">
          {/* Left Text */}
          <div>
            <span className="block text-[0.65rem] tracking-[0.2em] font-bold text-[#0BB8B8] uppercase mb-4">
              WHY CHOOSE US
            </span>

            <h2 className="font-extrabold text-[clamp(1.8rem,3vw,2.8rem)] leading-[1.2] tracking-[-0.02em] text-[#0A1520] mb-4">
              Are you looking for a dentist to give you that special smile?
            </h2>

            <p className="font-light text-[0.85rem] text-[#7A8BA0] leading-[1.8] max-w-lg mb-10">
              DenCli Dental Clinic provides the highest quality dental care with a team of experienced
              specialists, cutting-edge European equipment, and 100% transparent pricing — with zero
              hidden fees.
            </p>

            {/* Checklist */}
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-6 gap-y-3.5 mb-10">
              {checklist.map((item, idx) => (
                <div key={idx} className="flex items-center gap-2.5">
                  <CheckCircle2 className="w-4 h-4 text-[#0BB8B8] shrink-0" />
                  <span className="text-[0.82rem] text-[#0A1520] font-medium">
                    {item}
                  </span>
                </div>
              ))}
            </div>

            {/* CTA Button */}
            <div className="flex items-center gap-4">
              <button
                onClick={onOpenBooking}
                className="inline-flex items-center justify-center gap-2 shrink-0 whitespace-nowrap h-11 px-8 rounded-full text-[0.82rem] font-bold text-white bg-[#0A1520] hover:bg-[#1A2838] transition-all active:scale-[0.98] shadow-md"
              >
                <span>Meet Our Team</span>
                <ArrowRight className="w-4 h-4 text-[#0BB8B8]" />
              </button>
            </div>
          </div>

          {/* Right Image with Overlays */}
          <div className="relative">
            <div className="relative rounded-3xl overflow-hidden shadow-2xl border border-[#DDE4EE] group">
              <img
                src={DENTAL_IMAGES.team}
                alt="DenCli Dental Medical Team"
                className="w-full h-[420px] object-cover group-hover:scale-105 transition-transform duration-500 ease-out"
              />
              <div className="absolute inset-0 bg-gradient-to-t from-[#060C14]/60 via-transparent to-transparent pointer-events-none" />

              {/* Float Badge 1: Experience */}
              <div className="absolute bottom-6 left-6 bg-white/95 backdrop-blur-md rounded-2xl p-4 shadow-xl border border-white/40 flex items-center gap-3">
                <div className="w-10 h-10 rounded-full bg-[#EBF9F9] flex items-center justify-center text-[#0BB8B8]">
                  <Award className="w-5 h-5" />
                </div>
                <div>
                  <div className="text-[0.9rem] font-extrabold text-[#0A1520]">15+ Years</div>
                  <div className="text-[0.65rem] text-[#7A8BA0] font-medium">Clinical Excellence</div>
                </div>
              </div>

              {/* Float Badge 2: Safety */}
              <div className="absolute top-6 right-6 bg-[#060C14]/85 backdrop-blur-md rounded-2xl px-4 py-2.5 shadow-xl border border-white/10 flex items-center gap-2 text-white">
                <Shield className="w-4 h-4 text-[#0BB8B8]" />
                <span className="text-[0.72rem] font-semibold tracking-wide">ISO 9001:2015</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};
