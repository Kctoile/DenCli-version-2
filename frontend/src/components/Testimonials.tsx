import React from 'react';
import { Star, CheckCircle } from 'lucide-react';
import { DENTAL_IMAGES } from '../data/dentalData';

export const Testimonials: React.FC = () => {
  return (
    <section id="testimonials" className="bg-white py-24 px-6 lg:px-[60px]">
      <div className="max-w-[1200px] mx-auto">
        {/* Header */}
        <div className="text-center mb-14">
          <span className="text-[0.65rem] tracking-[0.2em] font-bold text-[#0BB8B8] uppercase">
            PATIENT REVIEWS
          </span>
          <h2 className="font-extrabold text-[clamp(1.8rem,3vw,2.8rem)] tracking-[-0.025em] text-[#0A1520] mt-3">
            Don&apos;t take our word for it.
          </h2>
          <div className="flex items-center justify-center gap-2 mt-2">
            <div className="flex text-[#F59E0B]">
              {[...Array(5)].map((_, i) => (
                <Star key={i} className="w-4 h-4 fill-[#F59E0B]" />
              ))}
            </div>
            <span className="text-[0.85rem] font-medium text-[#7A8BA0]">
              4.9 average from 1,200+ verified reviews
            </span>
          </div>
        </div>

        {/* 3 Review Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {DENTAL_IMAGES.testimonials.map((review, idx) => (
            <div
              key={idx}
              className="bg-[#EEF2F7]/70 rounded-2xl p-7 border border-[#DDE4EE] flex flex-col justify-between hover:shadow-lg hover:-translate-y-1 transition-all duration-300"
            >
              <div>
                {/* 5 Stars */}
                <div className="flex text-[#F59E0B] gap-1 mb-4">
                  {[...Array(review.rating)].map((_, i) => (
                    <Star key={i} className="w-4 h-4 fill-[#F59E0B]" />
                  ))}
                </div>

                {/* Quote */}
                <p className="font-normal text-[0.88rem] leading-[1.75] text-[#0A1520] italic">
                  &ldquo;{review.quote}&rdquo;
                </p>
              </div>

              {/* Attribution */}
              <div className="flex items-center gap-3 mt-8 pt-4 border-t border-[#DDE4EE]/60">
                <img
                  src={review.avatar}
                  alt={review.name}
                  className="w-11 h-11 rounded-full object-cover border-2 border-white shadow-sm"
                />
                <div>
                  <div className="font-bold text-[0.85rem] text-[#0A1520]">
                    {review.name}
                  </div>
                  <div className="text-[0.68rem] text-[#7A8BA0] flex items-center gap-1">
                    <CheckCircle className="w-3 h-3 text-[#10B981]" />
                    <span>Verified Patient</span>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};
