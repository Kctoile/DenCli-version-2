export interface ServiceItem {
  id: string;
  chip: string;
  title: string;
  description: string;
  image: string;
  accentBg: string;
}

export interface ProcessStep {
  number: string;
  title: string;
  description: string;
  image: string;
}

export interface Testimonial {
  quote: string;
  name: string;
  role: string;
  rating: number;
  avatar: string;
}

export interface FaqItem {
  question: string;
  answer: string;
}

export const DENTAL_IMAGES = {
  // Hero patient smiling in dental chair with gentle clinical lighting
  hero: "https://images.unsplash.com/photo-1629909613654-28e377c37b09?auto=format&fit=crop&w=2000&q=80",
  // Clinic interior / exam room
  clinicInterior: "https://images.unsplash.com/photo-1588776814546-1ffcf47267a5?auto=format&fit=crop&w=800&q=80",
  // Lead Doctor portrait
  leadDoctor: "https://images.unsplash.com/photo-1622253692010-333f2da6031d?auto=format&fit=crop&w=800&q=80",
  // Dental team in scrubs
  team: "https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?auto=format&fit=crop&w=1200&q=80",
  // Photo CTA banner
  ctaBanner: "https://images.unsplash.com/photo-1606811841689-23dfddce3e95?auto=format&fit=crop&w=2000&q=80",
  
  // 6 Services
  services: [
    {
      id: "checkup",
      chip: "FROM $80",
      title: "Dental Check-Up",
      description: "Comprehensive examination of your teeth, gums, and jaw. Includes digital 3D X-rays and a full personalized treatment plan.",
      image: "https://images.unsplash.com/photo-1588776814546-1ffcf47267a5?auto=format&fit=crop&w=800&q=80",
      accentBg: "bg-cyan-50/50"
    },
    {
      id: "cleaning",
      chip: "FROM $120",
      title: "Teeth Cleaning",
      description: "Professional scaling and Airflow polishing to remove plaque, tartar, and surface stains for a healthier, refreshed smile.",
      image: "https://images.unsplash.com/photo-1609840114035-3c981b782dfe?auto=format&fit=crop&w=800&q=80",
      accentBg: "bg-teal-50/50"
    },
    {
      id: "whitening",
      chip: "FROM $299",
      title: "Tooth Whitening",
      description: "Professional in-office laser whitening or custom at-home trays to brighten your smile safely by up to 8 shades.",
      image: "https://images.unsplash.com/photo-1598256989800-fe5f95da9787?auto=format&fit=crop&w=800&q=80",
      accentBg: "bg-amber-50/50"
    },
    {
      id: "implants",
      chip: "FROM $1,800",
      title: "Dental Implants",
      description: "Permanent, natural-looking titanium replacements for missing teeth — surgically placed with sub-millimeter precision.",
      image: "https://images.unsplash.com/photo-1588776814546-1ffcf47267a5?auto=format&fit=crop&w=800&q=80",
      accentBg: "bg-rose-50/50"
    },
    {
      id: "veneers",
      chip: "FROM $650",
      title: "Veneers & Crowns",
      description: "Custom-crafted ultrathin porcelain shells and zirconia crowns to restore shape, natural color, and lasting strength.",
      image: "https://images.unsplash.com/photo-1606811841689-23dfddce3e95?auto=format&fit=crop&w=800&q=80",
      accentBg: "bg-slate-50/50"
    },
    {
      id: "emergency",
      chip: "SAME DAY",
      title: "Emergency Care",
      description: "Immediate same-day emergency appointments for toothaches, fractured teeth, lost fillings, and sudden dental pain. Call anytime.",
      image: "https://images.unsplash.com/photo-1579684385127-1ef15d508118?auto=format&fit=crop&w=800&q=80",
      accentBg: "bg-red-50/50"
    }
  ],

  // 4 Process Steps
  process: [
    {
      number: "01",
      title: "Book Online",
      description: "Fill out our quick form or call us — our coordinators confirm your appointment slot within the hour.",
      image: "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=600&q=80"
    },
    {
      number: "02",
      title: "Welcome Visit",
      description: "Arrive at your scheduled time, enjoy our calming lounge, and meet your dedicated dental doctor.",
      image: "https://images.unsplash.com/photo-1576091160550-2173dba999ef?auto=format&fit=crop&w=600&q=80"
    },
    {
      number: "03",
      title: "Smile Assessment",
      description: "Digital panoramic X-rays, 3D scan, and a clear transparent care plan with exact pricing before we start.",
      image: "https://images.unsplash.com/photo-1588776814546-1ffcf47267a5?auto=format&fit=crop&w=600&q=80"
    },
    {
      number: "04",
      title: "Treatment & Care",
      description: "Gentle treatment completed, results reviewed together, and proactive aftercare guidance provided.",
      image: "https://images.unsplash.com/photo-1629909613654-28e377c37b09?auto=format&fit=crop&w=600&q=80"
    }
  ],

  // Testimonials
  testimonials: [
    {
      quote: "I've been going to DenCli for two years and I won't go anywhere else. They explained every step of my treatment clearly and I never felt pressured. Best dental experience I've had.",
      name: "Emily K.",
      role: "Patient · Veneer Treatment",
      rating: 5,
      avatar: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=200&q=80"
    },
    {
      quote: "As someone who used to dread the dentist, DenCli completely changed my experience. Painless, quick, and the results were incredible. My smile has never looked better.",
      name: "Marcus T.",
      role: "Patient · Implant Restoration",
      rating: 5,
      avatar: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=200&q=80"
    },
    {
      quote: "Brought my whole family here after a recommendation. The team is patient and gentle with my kids, which means everything. We're DenCli patients for life.",
      name: "Sarah L.",
      role: "Patient · Family Care",
      rating: 5,
      avatar: "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=200&q=80"
    }
  ],

  // FAQ
  faq: [
    {
      question: "Do you accept new patients?",
      answer: "Yes — we're always welcoming new patients and their families. You can book online, call us, or walk in during opening hours. Same-day appointments are often available."
    },
    {
      question: "Is dental treatment painful?",
      answer: "We prioritize your comfort at every step. All treatments are performed with modern computer-controlled local anesthesia so you don't feel pain. We also offer gentle sedation options for anxious patients."
    },
    {
      question: "How often should I come in for a check-up?",
      answer: "We recommend a dental check-up and professional cleaning every 6 months. Some patients with orthodontic appliances or gum conditions may benefit from visits every 3-4 months."
    },
    {
      question: "Do you offer payment plans & VNPay online payment?",
      answer: "Yes! We support 0% installment plans over 3 to 12 months, and seamless online payment via VNPay, credit cards, or direct insurance billing right inside our patient portal."
    },
    {
      question: "What should I do in a dental emergency?",
      answer: "Call our emergency hotline immediately at 1900 6868 or (800) 559-2648. We reserve priority emergency slots daily for acute toothaches, fractured teeth, lost crowns, or bleeding."
    },
    {
      question: "Can children be patients at DenCli?",
      answer: "Absolutely. We treat patients of all ages, including children from their first tooth onwards. Our pediatric specialists ensure a playful, fear-free dental experience for your little ones."
    },
    {
      question: "How long do treatments take?",
      answer: "It depends on the procedure: standard check-ups and hygiene take 45–60 minutes; laser teeth whitening takes 60 minutes; while implants or custom veneers are completed over 2 to 3 appointments."
    }
  ]
};
