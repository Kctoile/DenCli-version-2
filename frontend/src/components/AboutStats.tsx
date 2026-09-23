import React, { useEffect, useRef, useState } from 'react';
import { Star, ShieldCheck } from 'lucide-react';
import { DENTAL_IMAGES } from '../data/dentalData';

export const AboutStats: React.FC = () => {
  const [hasAnimated, setHasAnimated] = useState(false);
  const [satisfaction, setSatisfaction] = useState(0);
  const [smiles, setSmiles] = useState(0);
  const [rating, setRating] = useState('0.0');

  const statsRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        const [entry] = entries;
        if (entry.isIntersecting && !hasAnimated) {
          setHasAnimated(true);

          const startTime = performance.now();
          const duration = 1400; // 1,400ms

          const step = (now: number) => {
            const elapsed = now - startTime;
            const progress = Math.min(elapsed / duration, 1);
            // cubic-bezier(0.25, 0.1, 0.25, 1) approximation
            const easeProgress = 1 - Math.pow(1 - progress, 3);

            setSatisfaction(Math.round(easeProgress * 98));
            setSmiles(Math.round(easeProgress * 2400));
            setRating((easeProgress * 4.9).toFixed(1));

            if (progress < 1) {
              requestAnimationFrame(step);
            }
          };

          requestAnimationFrame(step);
        }
      },
      { threshold: 0.3 }
    );

    if (statsRef.current) {
      observer.observe(statsRef.current);
    }

    return () => observer.disconnect();
  }, [hasAnimated]);

  return (
    <section id="about" className="bg-white py-24 px-6 lg:px-[60px]">
      <div className="max-w-[1200px] mx-auto">
        {/* Section Label */}
        <div className="mb-8">
          <span className="text-[0.68rem] tracking-[0.2em] font-bold text-[#0BB8B8] uppercase">
            ABOUT US · DENCLI DENTAL
          </span>
        </div>

        {/* Large Editorial Statement */}
        <h2 className="font-bold text-[clamp(1.6rem,3.2vw,2.9rem)] leading-[1.3] tracking-[-0.02em] text-[#0A1520] max-w-[820px] mb-14">
          We deliver{' '}
          <span className="text-[#0BB8B8]">personalized dental treatments</span>{' '}
          with modern technology and gentle care ensuring healthy confident smiles for every patient.
        </h2>

        {/* Stats Row */}
        <div
          ref={statsRef}
          className="flex flex-col md:flex-row items-start md:items-center justify-between gap-8 py-8 border-t border-b border-[#DDE4EE] mb-12"
        >
          {/* Left Label */}
          <div className="text-[0.72rem] text-[#7A8BA0] max-w-[140px] leading-[1.6] font-normal">
            Thousands trust our team for lasting smiles!
          </div>

          {/* Stats Flex */}
          <div className="flex flex-wrap items-center gap-10 md:gap-14">
            {/* Stat 1 */}
            <div>
              <div className="font-extrabold text-[2.8rem] tracking-[-0.035em] text-[#0A1520] leading-[1]">
                {satisfaction}%
              </div>
              <div className="text-[0.72rem] text-[#7A8BA0] font-medium mt-1">
                Satisfaction Rate
              </div>
            </div>

            {/* Stat 2 */}
            <div>
              <div className="font-extrabold text-[2.8rem] tracking-[-0.035em] text-[#0A1520] leading-[1]">
                {smiles > 2000 ? '2k+' : smiles}
              </div>
              <div className="text-[0.72rem] text-[#7A8BA0] font-medium mt-1">
                Smiles Transformed
              </div>
            </div>

            {/* Stat 3 */}
            <div>
              <div className="font-extrabold text-[2.8rem] tracking-[-0.035em] text-[#0A1520] leading-[1] flex items-center gap-1">
                <span>{rating}</span>
                <span className="text-[#0BB8B8] text-[2.2rem]">★</span>
              </div>
              <div className="text-[0.72rem] text-[#7A8BA0] font-medium mt-1">
                Customer Rating
              </div>
            </div>
          </div>

          {/* Right: Small clinic photo */}
          <div className="hidden lg:block w-[200px] h-[130px] rounded-2xl overflow-hidden shrink-0 shadow-md">
            <img
              src={DENTAL_IMAGES.clinicInterior}
              alt="DenCli Dental Modern Clinic Room"
              className="w-full h-full object-cover hover:scale-105 transition-transform duration-300"
            />
          </div>
        </div>

        {/* Doctor Profile Card */}
        <div className="flex items-center gap-4 bg-[#EEF2F7]/50 border border-[#DDE4EE] rounded-2xl p-4 max-w-md hover:shadow-md transition-shadow">
          <img
            src={DENTAL_IMAGES.leadDoctor}
            alt="Dr. Daniel Nguyen"
            className="w-14 h-14 rounded-full object-cover border-2 border-white shadow-sm shrink-0"
          />
          <div>
            <div className="flex items-center gap-2">
              <h4 className="font-bold text-[0.95rem] text-[#0A1520]">
                Dr. Daniel Nguyen (TS. BS. Nguyễn Minh Đức)
              </h4>
              <ShieldCheck className="w-4 h-4 text-[#0BB8B8]" />
            </div>
            <p className="text-[0.74rem] text-[#7A8BA0] mt-0.5 flex items-center gap-1.5">
              <span>Lead Dental Specialist & Surgeon</span>
              <span>·</span>
              <span className="inline-flex items-center text-[#F59E0B] font-semibold">
                <Star className="w-3 h-3 fill-[#F59E0B] inline mr-0.5" /> 4.9 (120+ reviews)
              </span>
            </p>
          </div>
        </div>
      </div>
    </section>
  );
};
