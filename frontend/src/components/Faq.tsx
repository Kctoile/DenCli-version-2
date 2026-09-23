import React, { useState } from 'react';
import { ChevronDown } from 'lucide-react';
import { DENTAL_IMAGES } from '../data/dentalData';

export const Faq: React.FC = () => {
  const [openIndex, setOpenIndex] = useState<number | null>(0); // first item open by default

  const toggleItem = (index: number) => {
    setOpenIndex(openIndex === index ? null : index);
  };

  return (
    <section id="faq" className="bg-[#EEF2F7] py-24 px-6 lg:px-[60px]">
      <div className="max-w-3xl mx-auto">
        {/* Header */}
        <div className="text-center mb-14">
          <span className="text-[0.65rem] tracking-[0.2em] font-bold text-[#0BB8B8] uppercase">
            FAQ
          </span>
          <h2 className="font-extrabold text-[clamp(1.8rem,3vw,2.8rem)] tracking-[-0.025em] text-[#0A1520] mt-3">
            Common questions, clear answers.
          </h2>
          <p className="text-[0.85rem] text-[#7A8BA0] mt-2 font-normal">
            Everything you need to know about dental care at DenCli Dental Clinic.
          </p>
        </div>

        {/* 7 Accordion Items */}
        <div className="bg-white rounded-3xl p-6 sm:p-8 border border-[#DDE4EE] shadow-sm divide-y divide-[#DDE4EE]">
          {DENTAL_IMAGES.faq.map((item, idx) => {
            const isOpen = openIndex === idx;

            return (
              <div key={idx} className="py-4 first:pt-0 last:pb-0">
                <button
                  onClick={() => toggleItem(idx)}
                  className="w-full flex justify-between items-center text-left gap-4 py-2 focus:outline-none group cursor-pointer"
                >
                  <span className="font-bold text-[0.92rem] text-[#0A1520] group-hover:text-[#0BB8B8] transition-colors">
                    {item.question}
                  </span>
                  <div
                    className={`w-7 h-7 rounded-full flex items-center justify-center shrink-0 transition-transform duration-300 ${
                      isOpen ? 'bg-[#EBF9F9] rotate-180' : 'bg-slate-50 group-hover:bg-slate-100'
                    }`}
                  >
                    <ChevronDown className="w-4 h-4 text-[#0BB8B8]" />
                  </div>
                </button>

                <div
                  className={`overflow-hidden transition-all duration-300 ease-in-out ${
                    isOpen ? 'max-h-96 opacity-100 pt-2 pb-2' : 'max-h-0 opacity-0'
                  }`}
                >
                  <p className="font-light text-[0.84rem] text-[#7A8BA0] leading-relaxed">
                    {item.answer}
                  </p>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </section>
  );
};
