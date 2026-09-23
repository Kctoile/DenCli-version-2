import React from 'react';
import { Phone, Mail, MapPin, Globe } from 'lucide-react';

export const Footer: React.FC = () => {
  return (
    <footer className="bg-[#0A1520] text-white py-16 px-6 lg:px-[60px] border-t border-white/10">
      <div className="max-w-[1200px] mx-auto">
        {/* 4 Columns Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-12 mb-14">
          {/* Col 1: Brand */}
          <div>
            <div className="flex items-center gap-2.5 mb-2">
              <div className="w-8 h-8 bg-white/15 border border-white/25 rounded-[7px] flex items-center justify-center">
                <svg className="w-4 h-4 fill-[#0BB8B8]" viewBox="0 0 24 24">
                  <path d="M12 2C7.5 2 4 4.5 4 8c0 2.5 1.5 4.5 2.5 7 .8 2 1.5 5 3.5 5 1.5 0 2-3 2-3s.5 3 2 3c2 0 2.7-3 3.5-5 1-2.5 2.5-4.5 2.5-7 0-3.5-3.5-6-8-6z" />
                </svg>
              </div>
              <span className="font-extrabold text-[1.1rem] text-white tracking-[-0.01em]">
                DENCLI<span className="text-[#0BB8B8]">.</span>DENTAL
              </span>
            </div>

            <p className="font-light text-[0.7rem] text-white/40 tracking-[0.1em] uppercase">
              Your Family&apos;s Dental Clinic
            </p>

            <div className="mt-6 flex flex-col gap-2.5">
              <a
                href="tel:19006868"
                className="font-bold text-[0.88rem] text-white hover:text-[#0BB8B8] flex items-center gap-2 transition-colors"
              >
                <Phone className="w-4 h-4 text-[#0BB8B8]" />
                <span>1900 6868</span>
              </a>
              <a
                href="mailto:hello@dencli.vn"
                className="font-light text-[0.82rem] text-white/50 hover:text-white flex items-center gap-2 transition-colors"
              >
                <Mail className="w-4 h-4 text-[#0BB8B8]" />
                <span>hello@dencli.vn</span>
              </a>
              <p className="font-light text-[0.82rem] text-white/50 flex items-start gap-2">
                <MapPin className="w-4 h-4 text-[#0BB8B8] shrink-0 mt-0.5" />
                <span>Quận 1, TP. Hồ Chí Minh & Hoàn Kiếm, Hà Nội</span>
              </p>
            </div>
          </div>

          {/* Col 2: Services */}
          <div>
            <span className="block text-[0.62rem] tracking-[0.2em] font-bold uppercase text-white/35 mb-5">
              SERVICES
            </span>
            <ul className="space-y-3 font-normal text-[0.84rem] text-white/60">
              <li>
                <a href="#services" className="hover:text-white transition-colors">
                  Dental Check-Up & 3D Imaging
                </a>
              </li>
              <li>
                <a href="#services" className="hover:text-white transition-colors">
                  Teeth Cleaning & Airflow
                </a>
              </li>
              <li>
                <a href="#services" className="hover:text-white transition-colors">
                  Laser Tooth Whitening
                </a>
              </li>
              <li>
                <a href="#services" className="hover:text-white transition-colors">
                  Titanium Dental Implants
                </a>
              </li>
              <li>
                <a href="#services" className="hover:text-white transition-colors">
                  Porcelain Veneers & Crowns
                </a>
              </li>
              <li>
                <a href="#services" className="hover:text-white transition-colors">
                  24/7 Emergency Care
                </a>
              </li>
            </ul>
          </div>

          {/* Col 3: Clinic */}
          <div>
            <span className="block text-[0.62rem] tracking-[0.2em] font-bold uppercase text-white/35 mb-5">
              CLINIC
            </span>
            <ul className="space-y-3 font-normal text-[0.84rem] text-white/60">
              <li>
                <a href="#about" className="hover:text-white transition-colors">
                  About DenCli
                </a>
              </li>
              <li>
                <a href="#about" className="hover:text-white transition-colors">
                  Meet Our Doctors
                </a>
              </li>
              <li>
                <a href="#testimonials" className="hover:text-white transition-colors">
                  Patient Reviews
                </a>
              </li>
              <li>
                <a href="login" className="hover:text-[#0BB8B8] font-medium transition-colors">
                  Patient Portal Login →
                </a>
              </li>
              <li>
                <a href="register" className="hover:text-[#0BB8B8] font-medium transition-colors">
                  Register Patient Account
                </a>
              </li>
            </ul>
          </div>

          {/* Col 4: Opening Hours */}
          <div>
            <span className="block text-[0.62rem] tracking-[0.2em] font-bold uppercase text-white/35 mb-5">
              OPENING HOURS
            </span>
            <div className="space-y-2 text-[0.82rem] text-white/60">
              <div className="flex justify-between">
                <span>Mon – Fri:</span>
                <span className="text-white font-medium">8:00am – 7:00pm</span>
              </div>
              <div className="flex justify-between">
                <span>Saturday:</span>
                <span className="text-white font-medium">9:00am – 5:00pm</span>
              </div>
              <div className="flex justify-between">
                <span>Sunday:</span>
                <span className="text-[#0BB8B8] font-medium">Emergency Only</span>
              </div>
              <div className="pt-2 text-white/80 font-semibold text-[0.85rem]">
                24/7 Emergency: 1900 6868
              </div>
            </div>

            {/* Social Icons */}
            <div className="flex gap-4 mt-6">
              <a
                href="#"
                className="w-8 h-8 rounded-full bg-white/5 hover:bg-[#0BB8B8] flex items-center justify-center text-white/50 hover:text-white transition-all"
                aria-label="Facebook"
              >
                <svg className="w-4 h-4 fill-currentColor" viewBox="0 0 24 24">
                  <path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z" />
                </svg>
              </a>
              <a
                href="#"
                className="w-8 h-8 rounded-full bg-white/5 hover:bg-[#0BB8B8] flex items-center justify-center text-white/50 hover:text-white transition-all"
                aria-label="Instagram"
              >
                <svg className="w-4 h-4 fill-none stroke-currentColor stroke-2" viewBox="0 0 24 24">
                  <rect width="20" height="20" x="2" y="2" rx="5" ry="5" />
                  <path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z" />
                  <line x1="17.5" x2="17.51" y1="6.5" y2="6.5" />
                </svg>
              </a>
              <a
                href="#"
                className="w-8 h-8 rounded-full bg-white/5 hover:bg-[#0BB8B8] flex items-center justify-center text-white/50 hover:text-white transition-all"
                aria-label="Global Website"
              >
                <Globe className="w-4 h-4" />
              </a>
            </div>
          </div>
        </div>

        {/* Bottom Bar */}
        <div className="pt-8 border-t border-white/10 flex flex-col sm:flex-row justify-between items-center gap-4 text-xs text-white/40">
          <div>
            © 2026 DenCli Dental Clinic. All rights reserved. Powered by DenCli Smart Clinical System.
          </div>
          <div className="flex gap-6">
            <a href="#" className="hover:text-white/70 transition-colors">
              Privacy Policy
            </a>
            <a href="#" className="hover:text-white/70 transition-colors">
              Terms of Service
            </a>
            <a href="login" className="hover:text-[#0BB8B8] transition-colors">
              Doctor / Staff Login
            </a>
          </div>
        </div>
      </div>
    </footer>
  );
};
