import React, { useState, useEffect } from 'react';
import { Phone, Menu, X, ArrowRight, User } from 'lucide-react';

interface NavbarProps {
  onOpenBooking: () => void;
}

export const Navbar: React.FC<NavbarProps> = ({ onOpenBooking }) => {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const [isScrolled, setIsScrolled] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 20);
    };
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  return (
    <>
      <header
        className={`fixed top-0 left-0 right-0 z-40 transition-all duration-300 ${
          isScrolled
            ? 'bg-[#060C14]/90 backdrop-blur-md border-b border-white/10 py-3 shadow-lg'
            : 'bg-transparent py-5'
        }`}
      >
        <div className="max-w-[1200px] mx-auto px-6 lg:px-[60px] flex justify-between items-center w-full">
          {/* Left — Logo */}
          <a href="#" className="flex items-center gap-2.5 group">
            <div className="w-8 h-8 bg-white/15 border border-white/25 rounded-[7px] flex items-center justify-center backdrop-blur-sm group-hover:border-[#0BB8B8] transition-colors">
              <svg className="w-4 h-4 fill-white group-hover:fill-[#0BB8B8] transition-colors" viewBox="0 0 24 24">
                <path d="M12 2C7.5 2 4 4.5 4 8c0 2.5 1.5 4.5 2.5 7 .8 2 1.5 5 3.5 5 1.5 0 2-3 2-3s.5 3 2 3c2 0 2.7-3 3.5-5 1-2.5 2.5-4.5 2.5-7 0-3.5-3.5-6-8-6z" />
              </svg>
            </div>
            <div className="flex flex-col">
              <span className="font-extrabold text-[1.05rem] tracking-[-0.01em] text-white font-['Plus_Jakarta_Sans']">
                DENCLI<span className="text-[#0BB8B8]">.</span>DENTAL
              </span>
              <span className="text-[0.6rem] tracking-[0.14em] text-white/50 uppercase font-medium">
                Modern Care
              </span>
            </div>
          </a>

          {/* Center (desktop) */}
          <nav className="hidden md:flex items-center gap-8">
            <a
              href="#services"
              className="text-[0.78rem] font-medium text-white/70 hover:text-white transition-colors"
            >
              Services
            </a>
            <a
              href="#about"
              className="text-[0.78rem] font-medium text-white/70 hover:text-white transition-colors"
            >
              About Us
            </a>
            <a
              href="#process"
              className="text-[0.78rem] font-medium text-white/70 hover:text-white transition-colors"
            >
              How It Works
            </a>
            <a
              href="#testimonials"
              className="text-[0.78rem] font-medium text-white/70 hover:text-white transition-colors"
            >
              Testimonials
            </a>
            <a
              href="#faq"
              className="text-[0.78rem] font-medium text-white/70 hover:text-white transition-colors"
            >
              FAQ
            </a>
          </nav>

          {/* Right */}
          <div className="hidden md:flex items-center gap-5">
            <a
              href="tel:19006868"
              className="flex items-center gap-1.5 text-[0.78rem] text-white/70 hover:text-white transition-colors"
            >
              <Phone className="w-3.5 h-3.5 text-[#0BB8B8]" />
              <span className="font-semibold text-white/90">1900 6868</span>
            </a>

            {/* Patient Portal Link */}
            <a
              href="login"
              title="Portal Đăng nhập Bệnh nhân"
              className="inline-flex items-center gap-1.5 bg-white/10 hover:bg-white/15 text-white/80 hover:text-white text-[0.72rem] font-medium px-3.5 py-1.5 rounded-full border border-white/15 transition-all"
            >
              <User className="w-3.5 h-3.5 text-[#0BB8B8]" />
              <span>Portal</span>
            </a>

            {/* Book a Call Button */}
            <button
              onClick={onOpenBooking}
              className="inline-flex items-center justify-center gap-2 shrink-0 whitespace-nowrap h-9 px-5 text-[0.75rem] font-bold text-white bg-[#0BB8B8] hover:bg-[#099E9E] rounded-full shadow-lg shadow-[#0BB8B8]/25 transition-all active:scale-[0.98]"
            >
              Book a Call
            </button>
          </div>

          {/* Mobile Hamburger */}
          <div className="flex items-center gap-2 md:hidden">
            <button
              onClick={onOpenBooking}
              className="h-8 px-3.5 text-[0.7rem] font-bold text-white bg-[#0BB8B8] rounded-full"
            >
              Book
            </button>
            <button
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              className="p-2 text-white/80 hover:text-white"
              aria-label="Toggle menu"
            >
              {mobileMenuOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
            </button>
          </div>
        </div>
      </header>

      {/* Mobile Menu Overlay */}
      {mobileMenuOpen && (
        <div className="fixed inset-0 z-30 bg-[#060C14]/98 backdrop-blur-xl flex flex-col justify-center px-8 md:hidden">
          <div className="flex flex-col gap-6 text-center">
            <a
              href="#services"
              onClick={() => setMobileMenuOpen(false)}
              className="text-lg font-semibold text-white/80 hover:text-white"
            >
              Services
            </a>
            <a
              href="#about"
              onClick={() => setMobileMenuOpen(false)}
              className="text-lg font-semibold text-white/80 hover:text-white"
            >
              About Us
            </a>
            <a
              href="#process"
              onClick={() => setMobileMenuOpen(false)}
              className="text-lg font-semibold text-white/80 hover:text-white"
            >
              How It Works
            </a>
            <a
              href="#testimonials"
              onClick={() => setMobileMenuOpen(false)}
              className="text-lg font-semibold text-white/80 hover:text-white"
            >
              Testimonials
            </a>
            <a
              href="#faq"
              onClick={() => setMobileMenuOpen(false)}
              className="text-lg font-semibold text-white/80 hover:text-white"
            >
              FAQ
            </a>
            <div className="h-px bg-white/10 my-2" />
            <a
              href="tel:19006868"
              className="flex items-center justify-center gap-2 text-white font-medium text-base"
            >
              <Phone className="w-4 h-4 text-[#0BB8B8]" />
              Hotline: 1900 6868
            </a>
            <a
              href="login"
              className="inline-flex items-center justify-center gap-2 text-white/80 hover:text-white text-base py-2 border border-white/15 rounded-full"
            >
              <User className="w-4 h-4 text-[#0BB8B8]" />
              Patient Portal Login
            </a>
            <button
              onClick={() => {
                setMobileMenuOpen(false);
                onOpenBooking();
              }}
              className="inline-flex items-center justify-center gap-2 h-12 px-6 font-bold text-white bg-[#0BB8B8] rounded-full shadow-lg"
            >
              Book Appointment <ArrowRight className="w-4 h-4" />
            </button>
          </div>
        </div>
      )}
    </>
  );
};
