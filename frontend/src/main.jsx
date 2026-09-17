import React, { useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { motion, AnimatePresence } from 'framer-motion';

import {
  BarChart3,
  CheckCircle2,
  Clock3,
  FileWarning,
  LogIn,
  Menu,
  Plus,
  Search,
  ShieldCheck,
  TrendingUp,
  X
} from 'lucide-react';

import {
  Bar,
  BarChart,
  CartesianGrid,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
  PieChart,
  Pie,
  Cell
} from 'recharts';

import './index.css';

const API = (
  import.meta.env.VITE_API_URL ||
  'http://localhost:8080/api'
).replace(/\/$/, '');


/* =========================================================
   DEMO DATA
========================================================= */

const demoComplaints = [
  {
    id: 1,
    trackingNumber: 'GRV-2026-1042',
    title: 'Broken street lights on Main Road',
    category: { name: 'Street Lights' },
    priority: 'HIGH',
    status: 'IN_PROGRESS',
    location: 'Main Road',
    createdAt: '2026-09-14'
  },
  {
    id: 2,
    trackingNumber: 'GRV-2026-1041',
    title: 'Garbage collection delayed',
    category: { name: 'Garbage' },
    priority: 'MEDIUM',
    status: 'UNDER_REVIEW',
    location: 'Ward 12',
    createdAt: '2026-09-13'
  },
  {
    id: 3,
    trackingNumber: 'GRV-2026-1037',
    title: 'Potholes near bus stop',
    category: { name: 'Roads' },
    priority: 'HIGH',
    status: 'RESOLVED',
    location: 'Central Avenue',
    createdAt: '2026-09-10'
  },
  {
    id: 4,
    trackingNumber: 'GRV-2026-1030',
    title: 'Water supply interruption',
    category: { name: 'Water Supply' },
    priority: 'URGENT',
    status: 'SUBMITTED',
    location: 'Lake View',
    createdAt: '2026-09-08'
  }
];


/* =========================================================
   BUTTON
========================================================= */

function Button({
  children,
  onClick,
  secondary = false
}) {
  return (
    <button
      onClick={onClick}
      className={
        (secondary
          ? 'glass text-white'
          : 'bg-white text-black') +
        ' rounded-xl px-5 py-3 font-bold hover:scale-[1.02] transition'
      }
    >
      {children}
    </button>
  );
}


/* =========================================================
   NAVBAR
========================================================= */

function Nav({ go, user, onLogout }) {
  return (
    <nav className="sticky top-0 z-40 p-4">
      <div className="glass max-w-7xl mx-auto rounded-2xl px-5 py-3 flex items-center justify-between">

        <button
          onClick={() => go('home')}
          className="font-black text-xl flex gap-2 items-center"
        >
          <ShieldCheck className="text-white" />
          CivicResolve
        </button>

        <div className="hidden md:flex items-center gap-2 text-sm">

          <button
            onClick={() => go('home')}
            className="px-3 py-2"
          >
            Home
          </button>

          <button
            onClick={() => go('track')}
            className="px-3 py-2"
          >
            Track
          </button>

          {user?.role !== 'ADMIN' && user?.role !== 'AUTHORITY' && (
            <button
              onClick={() => go('submit')}
              className="px-3 py-2"
            >
              Submit
            </button>
          )}

          {(user?.role === 'ADMIN' || user?.role === 'AUTHORITY') && (
            <button
              onClick={() => go('admin')}
              className="px-3 py-2"
            >
              Authority
            </button>
          )}

          {user ? (
            <button
              onClick={onLogout}
              className="glass px-4 py-2 rounded-xl"
            >
              Logout
            </button>
          ) : (
            <Button onClick={() => go('login')}>
              Login
            </Button>
          )}

        </div>

        <button className="md:hidden">
          <Menu />
        </button>

      </div>
    </nav>
  );
}


/* =========================================================
   HOME
========================================================= */

function Home({ go }) {
  return (
    <motion.section
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="max-w-7xl mx-auto px-5 pt-16 pb-20"
    >

      <div className="grid lg:grid-cols-[1.15fr_.85fr] gap-12 items-center">

        <div>

          <span className="glass inline-flex rounded-full px-4 py-2 text-sm text-neutral-200">
            Transparent civic service platform
          </span>

          <h1 className="text-6xl md:text-8xl font-black leading-[.9] mt-6">
            Your voice.
            <br />
            <span className="text-white">
              Visible action.
            </span>
          </h1>

          <p className="text-neutral-300 text-lg max-w-2xl mt-7 leading-relaxed">
            Submit public grievances, track progress in real time,
            and keep authorities accountable with a transparent
            digital workflow.
          </p>

          <div className="flex flex-wrap gap-3 mt-8">

            <Button onClick={() => go('submit')}>
              <Plus
                className="inline mr-2"
                size={18}
              />
              Report an issue
            </Button>

            <Button
              secondary
              onClick={() => go('track')}
            >
              Track complaint
            </Button>

          </div>

        </div>


        <div className="glass rounded-[32px] p-7">

          <div className="text-sm text-neutral-400">
            Live service overview
          </div>

          <div className="grid grid-cols-2 gap-4 mt-5">

            {[
              ['12,842', 'Total complaints'],
              ['9,821', 'Resolved'],
              ['1,245', 'In progress'],
              ['87', 'Priority cases']
            ].map((x) => (
              <div
                key={x[1]}
                className="rounded-2xl bg-white/5 p-5"
              >
                <div className="text-3xl font-black">
                  {x[0]}
                </div>

                <div className="text-neutral-400 text-sm mt-1">
                  {x[1]}
                </div>
              </div>
            ))}

          </div>

          <div className="mt-5 rounded-2xl bg-white/5 border border-white/15 p-5">

            <div className="flex items-center gap-3">

              <CheckCircle2 className="text-white" />

              <div>
                <b>Resolution transparency</b>

                <p className="text-sm text-neutral-400 mt-1">
                  Every status change can be recorded in
                  the complaint timeline.
                </p>
              </div>

            </div>

          </div>

        </div>

      </div>

    </motion.section>
  );
}


/* =========================================================
   LOGIN / REGISTER
========================================================= */

function Auth({ mode, go, setUser }) {

  const [form, setForm] = useState({
    name: '',
    email: '',
    password: ''
  });

  const [msg, setMsg] = useState('');

  const submit = async (e) => {

    e.preventDefault();
    setMsg('');

    try {

      const endpoint =
        mode === 'login'
          ? '/auth/login'
          : '/auth/register';

      const body =
        mode === 'login'
          ? {
              email: form.email,
              password: form.password
            }
          : {
              fullName: form.name,
              email: form.email,
              password: form.password
            };

      const response = await fetch(
        API + endpoint,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(body)
        }
      );

      const text = await response.text();

      let data = null;

      if (text.trim()) {
        try {
          data = JSON.parse(text);
        } catch {
          data = null;
        }
      }

      if (!response.ok) {
        throw new Error(
          data?.message ||
          data?.error ||
          'Request failed'
        );
      }

      if (mode === 'login') {

        localStorage.setItem(
          'token',
          data.token
        );

        setUser({
          name: data.name,
          role: data.role,
          userId: data.userId
        });

        go(
          data.role === 'ADMIN' || data.role === 'AUTHORITY'
            ? 'admin'
            : 'citizen'
        );

      } else {

        setMsg(
          'Account created. You can now sign in.'
        );
      }

    } catch (error) {

      setMsg(
        error instanceof TypeError
          ? 'Unable to reach CivicResolve. Check your connection and try again.'
          : error.message ||
        'Something went wrong.'
      );

    }
  };


  return (
    <section className="max-w-md mx-auto px-5 pt-16">

      <div className="glass rounded-3xl p-7">

        <h2 className="text-4xl font-black">
          {mode === 'login'
            ? 'Welcome back'
            : 'Create account'}
        </h2>

        <p className="text-neutral-400 mt-2">
          {mode === 'login'
            ? 'Access your civic workspace.'
            : 'Join CivicResolve as a citizen.'}
        </p>

        <form
          onSubmit={submit}
          className="space-y-4 mt-7"
        >

          {mode === 'register' && (
            <input
              className="input"
              placeholder="Full name"
              required
              value={form.name}
              onChange={(e) =>
                setForm({
                  ...form,
                  name: e.target.value
                })
              }
            />
          )}

          <input
            className="input"
            type="email"
            placeholder="Email address"
            required
            value={form.email}
            onChange={(e) =>
              setForm({
                ...form,
                email: e.target.value
              })
            }
          />

          <input
            className="input"
            type="password"
            placeholder="Password"
            required
            minLength="8"
            value={form.password}
            onChange={(e) =>
              setForm({
                ...form,
                password: e.target.value
              })
            }
          />

          <Button>
            {mode === 'login'
              ? 'Sign in'
              : 'Create account'}
          </Button>

        </form>

        {msg && (
          <p className="text-neutral-200 mt-4 text-sm">
            {msg}
          </p>
        )}

        <button
          className="text-sm text-neutral-400 mt-6"
          onClick={() =>
            go(
              mode === 'login'
                ? 'register'
                : 'login'
            )
          }
        >
          {mode === 'login'
            ? 'New here? Create an account'
            : 'Already registered? Sign in'}
        </button>

      </div>

    </section>
  );
}


/* =========================================================
   TRACK COMPLAINT
========================================================= */

function Track() {

  const [code, setCode] = useState('');
  const [data, setData] = useState(null);
  const [msg, setMsg] = useState('');

  const searchComplaint = async () => {

    try {

      const response = await fetch(
        API + '/complaints/track/' + code
      );

      if (!response.ok) {
        const error = await response.json().catch(() => null);
        throw new Error(error?.message || 'No complaint found.');
      }

      const data = await response.json();

      setData(data);
      setMsg('');

    } catch (error) {
      setData(null);
      setMsg(error.message || 'No complaint found.');
    }
  };


  return (
    <section className="max-w-4xl mx-auto px-5 pt-14">

      <h2 className="text-5xl font-black">
        Track your complaint
      </h2>

      <p className="text-neutral-400 mt-2">
        Use the tracking number from your submission.
      </p>

      <div className="glass rounded-3xl p-5 mt-7 flex gap-3">

        <input
          className="input"
          placeholder="GRV-2026-1042"
          value={code}
          onChange={(e) =>
            setCode(e.target.value)
          }
        />

        <Button onClick={searchComplaint}>
          <Search />
        </Button>

      </div>

      {msg && (
        <p className="text-neutral-300 mt-4">
          {msg}
        </p>
      )}

      {data && (
        <div className="glass rounded-3xl p-7 mt-6">

          <div className="flex flex-wrap justify-between gap-3">

            <div>

              <div className="text-sm text-white">
                {data.complaint.trackingNumber}
              </div>

              <h3 className="text-2xl font-bold mt-1">
                {data.complaint.title}
              </h3>

            </div>

            <span className="bg-white/10 text-neutral-200 px-3 py-2 rounded-full h-fit">
              {data.complaint.status}
            </span>

          </div>

          <p className="text-neutral-400 mt-4">
            {data.complaint.description}
          </p>

          <div className="border-l border-white/10 ml-3 mt-7 space-y-6">

            {data.history.map((h, i) => (
              <div
                key={i}
                className="relative pl-7"
              >

                <span className="absolute -left-[7px] top-1 w-3 h-3 rounded-full bg-white" />

                <b>
                  {h.newStatus}
                </b>

                <p className="text-sm text-neutral-400 mt-1">
                  {h.comment}
                </p>

              </div>
            ))}

          </div>

        </div>
      )}

    </section>
  );
}


/* =========================================================
   SUBMIT COMPLAINT
========================================================= */

function Submit({ user, go }) {

  const [form, setForm] = useState({
    title: '',
    description: '',
    category: '',
    priority: 'MEDIUM',
    location: ''
  });

  const [msg, setMsg] = useState('');
  const [loading, setLoading] = useState(false);
  const [categories, setCategories] = useState([]);
  const [categoriesLoading, setCategoriesLoading] = useState(true);

  React.useEffect(() => {
    let active = true;
    fetch(API + '/categories')
      .then((response) => {
        if (!response.ok) throw new Error('Categories could not be loaded.');
        return response.json();
      })
      .then((items) => {
        if (!Array.isArray(items) || !items.every((item) => item && Number.isInteger(item.id) && item.id > 0 && typeof item.name === 'string')) {
          throw new Error('Categories returned an invalid response.');
        }
        if (!active) return;
        setCategories(items);
        if (items.length) setForm((current) => ({ ...current, category: String(items[0].id) }));
        else setMsg('No grievance categories are available yet. Please try again shortly.');
      })
      .catch((error) => active && setMsg(error.message))
      .finally(() => active && setCategoriesLoading(false));
    return () => { active = false; };
  }, []);


  const send = async (e) => {

    e.preventDefault();

    if (!user) {
      go('login');
      return;
    }

    const categoryId = Number(form.category);
    if (!Number.isInteger(categoryId) || !categories.some((category) => category.id === categoryId)) {
      setMsg('Choose one of the available categories before submitting.');
      return;
    }

    setLoading(true);
    setMsg('');


    try {

      const response = await fetch(
        API + '/complaints',
        {
          method: 'POST',

          headers: {
            'Content-Type': 'application/json',
            'Authorization':
              'Bearer ' +
              localStorage.getItem('token')
          },

          body: JSON.stringify({
            title: form.title,
            description: form.description,
            priority: form.priority,
            location: form.location,

            categoryId
          })
        }
      );


      /*
        IMPORTANT:
        Read the response as text first.
        This prevents:
        "Unexpected end of JSON input"
      */

      const responseText =
        await response.text();

      let data = null;

      if (responseText.trim()) {

        try {
          data = JSON.parse(responseText);
        } catch {
          data = null;
        }

      }


      if (!response.ok) {

        throw new Error(
          data?.message ||
          data?.error ||
          responseText ||
          `Server error (${response.status})`
        );

      }


      if (!data) {

        throw new Error(
          'The server accepted the request but returned an empty response.'
        );

      }


      setMsg(
        'Complaint submitted successfully! Tracking number: ' +
        data.trackingNumber
      );


      setForm({
        title: '',
        description: '',
        category: categories[0] ? String(categories[0].id) : '',
        priority: 'MEDIUM',
        location: ''
      });


    } catch (error) {

      console.error(
        'Complaint submission error:',
        error
      );

      setMsg(
        error.message ||
        'Unable to submit complaint.'
      );

    } finally {

      setLoading(false);

    }
  };


  return (
    <section className="max-w-3xl mx-auto px-5 pt-14">

      <h2 className="text-5xl font-black">
        Report a public issue
      </h2>

      <p className="text-neutral-400 mt-2">
        Provide enough detail for an authority to act quickly.
      </p>


      <form
        onSubmit={send}
        className="glass rounded-3xl p-7 mt-7 space-y-5"
      >

        <input
          className="input"
          placeholder="Complaint title"
          required
          value={form.title}
          onChange={(e) =>
            setForm({
              ...form,
              title: e.target.value
            })
          }
        />


        <textarea
          className="input min-h-40"
          placeholder="Describe the issue, what happened and where..."
          required
          value={form.description}
          onChange={(e) =>
            setForm({
              ...form,
              description: e.target.value
            })
          }
        />


        <div className="grid md:grid-cols-2 gap-4">

          <select
            className="input"
            required
            disabled={categoriesLoading || !categories.length}
            value={form.category}
            onChange={(e) =>
              setForm({
                ...form,
                category: e.target.value
              })
            }
          >
            {!categories.length && <option value="">{categoriesLoading ? 'Loading categories…' : 'No categories available'}</option>}
            {categories.map((category) => (
              <option
                key={category.id}
                className="bg-black"
                value={category.id}
              >
                {category.name}
              </option>
            ))}

          </select>


          <select
            className="input"
            value={form.priority}
            onChange={(e) =>
              setForm({
                ...form,
                priority: e.target.value
              })
            }
          >

            {[
              'LOW',
              'MEDIUM',
              'HIGH',
              'URGENT'
            ].map((priority) => (
              <option
                key={priority}
                className="bg-black"
                value={priority}
              >
                {priority}
              </option>
            ))}

          </select>

        </div>


        <input
          className="input"
          placeholder="Location / landmark"
          value={form.location}
          onChange={(e) =>
            setForm({
              ...form,
              location: e.target.value
            })
          }
        />


        <button
          type="submit"
          disabled={loading}
          className="rounded-xl bg-white text-black px-5 py-3 font-bold hover:scale-[1.02] transition disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {loading
            ? 'Submitting...'
            : 'Submit grievance'}
        </button>


        {msg && (
          <p className="text-neutral-200">
            {msg}
          </p>
        )}

      </form>

    </section>
  );
}


/* =========================================================
   CITIZEN DASHBOARD
========================================================= */

function Citizen({ user, go }) {

  const [complaints, setComplaints] = useState([]);
  const [msg, setMsg] = useState('');
  const [loading, setLoading] = useState(true);

  React.useEffect(() => {
    const load = async () => {
      try {
        const response = await fetch(API + '/complaints/mine', {
          headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
        });
        const data = await response.json().catch(() => null);
        if (!response.ok) throw new Error(data?.message || 'Could not load your complaints.');
        setComplaints(data);
      } catch (error) {
        setMsg(error.message || 'Could not load your complaints.');
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const counts = complaints.reduce((result, complaint) => {
    result[complaint.status] = (result[complaint.status] || 0) + 1;
    return result;
  }, {});

  return (
    <section className="max-w-7xl mx-auto px-5 pt-12">

      <div className="flex justify-between items-end">

        <div>

          <div className="text-white text-sm">
            Citizen workspace
          </div>

          <h2 className="text-5xl font-black mt-1">
            Hello, {user.name}
          </h2>

        </div>

        <Button onClick={() => go('submit')}>
          + New complaint
        </Button>

      </div>


      <div className="grid md:grid-cols-3 gap-4 mt-8">

        {[
          [counts.SUBMITTED || 0, 'Submitted'],
          [counts.IN_PROGRESS || 0, 'In progress'],
          [counts.RESOLVED || 0, 'Resolved']
        ].map((x) => (
          <div
            key={x[1]}
            className="glass rounded-2xl p-6"
          >

            <div className="text-3xl font-black">
              {x[0]}
            </div>

            <div className="text-neutral-400">
              {x[1]}
            </div>

          </div>
        ))}

      </div>


      <div className="glass rounded-3xl p-6 mt-6">

        <h3 className="text-xl font-bold">
          Recent complaints
        </h3>

        <div className="mt-4 space-y-3">

          {complaints.map((c) => (
            <div
              key={c.id}
              className="rounded-2xl bg-white/5 p-4 flex flex-wrap justify-between gap-3"
            >

              <div>

                <div className="text-xs text-white">
                  {c.trackingNumber}
                </div>

                <b>
                  {c.title}
                </b>

                <div className="text-sm text-neutral-500 mt-1">
                  {c.category.name} · {c.createdAt}
                </div>

              </div>

              <span className="h-fit rounded-full bg-white/10 px-3 py-1 text-sm">
                <StatusPill status={c.status} />
              </span>

            </div>
          ))}

          {loading && <div className="text-neutral-500 py-4">Loading your complaints…</div>}
          {!loading && !complaints.length && !msg && <div className="text-neutral-500 py-4">No complaints submitted yet.</div>}
          {msg && <div className="text-neutral-300 py-4">{msg}</div>}

        </div>

      </div>

    </section>
  );
}


/* =========================================================
   STATUS PILL
========================================================= */

function StatusPill({ status }) {

  const labels = {
    SUBMITTED: 'Submitted',
    UNDER_REVIEW: 'Under review',
    IN_PROGRESS: 'In progress',
    RESOLVED: 'Resolved'
  };

  return (
    <span className="rounded-full bg-white/10 px-3 py-1 text-xs font-bold">
      {labels[status] || status}
    </span>
  );
}


/* =========================================================
   ADMIN DASHBOARD
========================================================= */

function Admin() {

  const [complaints, setComplaints] = useState([]);

  const [stats, setStats] = useState({
    total: 0,
    submitted: 0,
    underReview: 0,
    inProgress: 0,
    resolved: 0
  });

  const [selected, setSelected] = useState(null);

  const [newStatus, setNewStatus] =
    useState('UNDER_REVIEW');

  const [comment, setComment] = useState('');
  const [msg, setMsg] = useState('');
  const [loading, setLoading] = useState(false);

  const token =
    localStorage.getItem('token');


  const chart = [
    {
      name: 'Submitted',
      value: stats.submitted
    },
    {
      name: 'Review',
      value: stats.underReview
    },
    {
      name: 'Progress',
      value: stats.inProgress
    },
    {
      name: 'Resolved',
      value: stats.resolved
    }
  ];


  const load = async () => {

    setMsg('');

    try {

      const headers = {
        Authorization:
          'Bearer ' + token
      };


      const [
        analyticsResponse,
        complaintsResponse
      ] = await Promise.all([
        fetch(
          API + '/admin/analytics',
          { headers }
        ),
        fetch(
          API + '/admin/complaints',
          { headers }
        )
      ]);


      if (
        analyticsResponse.status === 403 ||
        complaintsResponse.status === 403
      ) {
        throw new Error(
          'Authority access denied. Please log in again with an ADMIN account.'
        );
      }


      if (
        !analyticsResponse.ok ||
        !complaintsResponse.ok
      ) {
        throw new Error(
          'Could not load authority data.'
        );
      }


      setStats(
        await analyticsResponse.json()
      );

      setComplaints(
        await complaintsResponse.json()
      );


    } catch (error) {

      setMsg(error.message);

    }
  };


  React.useEffect(() => {
    load();
  }, []);


  const openComplaint = (complaint) => {

    setSelected(complaint);

    setNewStatus(
      complaint.status === 'SUBMITTED'
        ? 'UNDER_REVIEW'
        : complaint.status === 'UNDER_REVIEW'
        ? 'IN_PROGRESS'
        : complaint.status === 'IN_PROGRESS'
        ? 'RESOLVED'
        : complaint.status
    );

    setComment('');
    setMsg('');
  };


  const updateStatus = async () => {

    if (!selected) return;

    setLoading(true);
    setMsg('');

    try {

      const response = await fetch(
        API +
        '/admin/complaints/' +
        selected.id +
        '/status',
        {
          method: 'PATCH',

          headers: {
            'Content-Type':
              'application/json',
            'Authorization':
              'Bearer ' + token
          },

          body: JSON.stringify({
            status: newStatus,
            comment:
              comment.trim() ||
              'Status updated by authority'
          })
        }
      );


      const text =
        await response.text();

      let data = null;

      try {
        data = text
          ? JSON.parse(text)
          : null;
      } catch {
        data = null;
      }


      if (!response.ok) {

        throw new Error(
          data?.message ||
          text ||
          'Status update failed.'
        );

      }


      setMsg(
        'Status updated successfully.'
      );

      setSelected(data);

      setComplaints((previous) =>
        previous.map((complaint) =>
          complaint.id === data.id
            ? data
            : complaint
        )
      );

      setComment('');

      await load();


    } catch (error) {

      setMsg(error.message);

    } finally {

      setLoading(false);

    }
  };


  return (
    <section className="max-w-7xl mx-auto px-5 pt-10 pb-20">

      <div>

        <div className="text-white text-sm">
          Authority console
        </div>

        <h2 className="text-5xl font-black">
          Operations dashboard
        </h2>

        <p className="text-neutral-400 mt-2">
          Review complaints, update progress and
          keep citizens informed.
        </p>

      </div>


      {msg && (
        <div className="mt-5 rounded-2xl border border-white/20 bg-white/5 text-neutral-100 px-5 py-4">
          {msg}
        </div>
      )}


      <div className="grid grid-cols-2 lg:grid-cols-5 gap-4 mt-8">

        {[
          ['Total', stats.total],
          ['Submitted', stats.submitted],
          ['Review', stats.underReview],
          ['In progress', stats.inProgress],
          ['Resolved', stats.resolved]
        ].map((x) => (

          <div
            key={x[0]}
            className="glass rounded-2xl p-5"
          >

            <div className="text-2xl font-black">
              {x[1]}
            </div>

            <div className="text-sm text-neutral-400">
              {x[0]}
            </div>

          </div>

        ))}

      </div>


      <div className="grid lg:grid-cols-2 gap-5 mt-5">

        <div className="glass rounded-3xl p-6">

          <h3 className="font-bold text-lg">
            Resolution pipeline
          </h3>

          <div className="h-72 mt-4">

            <ResponsiveContainer>

              <BarChart data={chart}>

                <CartesianGrid
                  strokeDasharray="3 3"
                  strokeOpacity={0.1}
                />

                <XAxis dataKey="name" />

                <YAxis />

                <Tooltip />

                <Bar
                  dataKey="value"
                  fill="#e5e5e5"
                  radius={[8, 8, 0, 0]}
                />

              </BarChart>

            </ResponsiveContainer>

          </div>

        </div>


        <div className="glass rounded-3xl p-6">

          <h3 className="font-bold text-lg">
            Priority distribution
          </h3>

          <div className="h-72 mt-4">

            <ResponsiveContainer>

              <PieChart>

                <Pie
                  data={[
                    {
                      name: 'Urgent',
                      value: 12
                    },
                    {
                      name: 'High',
                      value: 28
                    },
                    {
                      name: 'Medium',
                      value: 42
                    },
                    {
                      name: 'Low',
                      value: 18
                    }
                  ]}
                  dataKey="value"
                  nameKey="name"
                  outerRadius={95}
                  label
                >

                  {[
                    '#f5f5f5',
                    '#d4d4d4',
                    '#a3a3a3',
                    '#737373'
                  ].map((color, index) => (
                    <Cell
                      key={index}
                      fill={color}
                    />
                  ))}

                </Pie>

                <Tooltip />

              </PieChart>

            </ResponsiveContainer>

          </div>

        </div>

      </div>


      <div className="glass rounded-3xl p-6 mt-5">

        <div className="flex justify-between items-center">

          <div>

            <h3 className="font-bold text-lg">
              Complaint queue
            </h3>

            <p className="text-sm text-neutral-500 mt-1">
              Select a complaint to review and
              update its status.
            </p>

          </div>

          <button
            onClick={load}
            className="text-sm text-white"
          >
            Refresh
          </button>

        </div>


        <div className="overflow-x-auto mt-4">

          <table className="w-full text-left text-sm">

            <thead className="text-neutral-500">

              <tr>

                <th className="py-3">
                  Tracking
                </th>

                <th>
                  Complaint
                </th>

                <th>
                  Priority
                </th>

                <th>
                  Status
                </th>

                <th>
                  Action
                </th>

              </tr>

            </thead>


            <tbody>

              {complaints.map((complaint) => (

                <tr
                  key={complaint.id}
                  className="border-t border-white/5"
                >

                  <td className="py-4 text-white">
                    {complaint.trackingNumber}
                  </td>

                  <td>

                    <button
                      onClick={() =>
                        openComplaint(complaint)
                      }
                      className="text-left hover:text-white"
                    >

                      <b>
                        {complaint.title}
                      </b>

                      <div className="text-xs text-neutral-500 mt-1">
                        {complaint.location ||
                          'Location not provided'}
                      </div>

                    </button>

                  </td>

                  <td>
                    {complaint.priority}
                  </td>

                  <td>
                    <StatusPill
                      status={complaint.status}
                    />
                  </td>

                  <td>

                    <button
                      onClick={() =>
                        openComplaint(complaint)
                      }
                      className="rounded-xl bg-white text-black px-4 py-2 font-bold hover:scale-[1.02] transition"
                    >
                      Manage
                    </button>

                  </td>

                </tr>

              ))}

            </tbody>

          </table>


          {complaints.length === 0 && (
            <div className="py-10 text-center text-neutral-500">
              No complaints found.
            </div>
          )}

        </div>

      </div>


      <AnimatePresence>

        {selected && (

          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className="fixed inset-0 z-50 bg-black/70 backdrop-blur-sm p-4 flex items-center justify-center"
            onMouseDown={(e) => {
              if (
                e.target === e.currentTarget
              ) {
                setSelected(null);
              }
            }}
          >

            <motion.div
              initial={{
                opacity: 0,
                y: 20,
                scale: 0.98
              }}
              animate={{
                opacity: 1,
                y: 0,
                scale: 1
              }}
              className="glass w-full max-w-2xl rounded-3xl p-7 max-h-[90vh] overflow-y-auto"
            >

              <div className="flex justify-between gap-4">

                <div>

                  <div className="text-sm text-white">
                    {selected.trackingNumber}
                  </div>

                  <h3 className="text-3xl font-black mt-1">
                    {selected.title}
                  </h3>

                </div>

                <button
                  onClick={() =>
                    setSelected(null)
                  }
                  className="rounded-xl bg-white/5 p-2"
                >
                  <X />
                </button>

              </div>


              <div className="grid md:grid-cols-2 gap-4 mt-6">

                <div className="rounded-2xl bg-white/5 p-4">

                  <div className="text-xs text-neutral-500">
                    Category
                  </div>

                  <div className="font-bold mt-1">
                    {selected.category?.name || '—'}
                  </div>

                </div>


                <div className="rounded-2xl bg-white/5 p-4">

                  <div className="text-xs text-neutral-500">
                    Priority
                  </div>

                  <div className="font-bold mt-1">
                    {selected.priority}
                  </div>

                </div>


                <div className="rounded-2xl bg-white/5 p-4 md:col-span-2">

                  <div className="text-xs text-neutral-500">
                    Location
                  </div>

                  <div className="font-bold mt-1">
                    {selected.location ||
                      'Not provided'}
                  </div>

                </div>

              </div>


              <div className="mt-6">

                <div className="text-sm text-neutral-500">
                  Description
                </div>

                <p className="mt-2 text-neutral-200 leading-relaxed">
                  {selected.description ||
                    'No description available.'}
                </p>

              </div>


              <div className="mt-7 border-t border-white/10 pt-6">

                <h4 className="font-bold text-lg">
                  Update complaint status
                </h4>


                <div className="grid md:grid-cols-2 gap-4 mt-4">

                  <select
                    className="input"
                    value={newStatus}
                    onChange={(e) =>
                      setNewStatus(
                        e.target.value
                      )
                    }
                  >

                    <option
                      className="bg-black"
                      value="SUBMITTED"
                    >
                      Submitted
                    </option>

                    <option
                      className="bg-black"
                      value="UNDER_REVIEW"
                    >
                      Under review
                    </option>

                    <option
                      className="bg-black"
                      value="IN_PROGRESS"
                    >
                      In progress
                    </option>

                    <option
                      className="bg-black"
                      value="RESOLVED"
                    >
                      Resolved
                    </option>

                  </select>


                  <div className="rounded-xl bg-white/5 px-4 py-3 flex items-center">

                    <StatusPill
                      status={selected.status}
                    />

                  </div>

                </div>


                <textarea
                  className="input min-h-28 mt-4"
                  placeholder="Authority comment for the citizen timeline..."
                  value={comment}
                  onChange={(e) =>
                    setComment(e.target.value)
                  }
                />


                <div className="flex flex-wrap gap-3 mt-4">

                  <button
                    onClick={updateStatus}
                    disabled={loading}
                    className="rounded-xl bg-white text-black px-5 py-3 font-bold disabled:opacity-50"
                  >
                    {loading
                      ? 'Updating...'
                      : 'Save status update'}
                  </button>

                  <button
                    onClick={() =>
                      setSelected(null)
                    }
                    className="glass text-white rounded-xl px-5 py-3 font-bold"
                  >
                    Close
                  </button>

                </div>

              </div>

            </motion.div>

          </motion.div>

        )}

      </AnimatePresence>

    </section>
  );
}


/* =========================================================
   APP
========================================================= */

function App() {

  const [page, setPage] =
    useState('home');

  const [user, setUser] =
    useState(null);

  React.useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) return;
    fetch(API + '/auth/me', { headers: { Authorization: 'Bearer ' + token } })
      .then(async (response) => {
        const data = await response.json().catch(() => null);
        if (!response.ok || !data) throw new Error('Saved session is no longer valid.');
        setUser({ name: data.name, role: data.role, userId: data.userId });
        setPage(data.role === 'ADMIN' || data.role === 'AUTHORITY' ? 'admin' : 'citizen');
      })
      .catch(() => localStorage.removeItem('token'));
  }, []);


  const go = (pageName) => {
    setPage(pageName);
  };


  const logout = () => {

    localStorage.removeItem('token');

    setUser(null);

    go('home');
  };


  return (
    <>
      <Nav
        go={go}
        user={user}
        onLogout={logout}
      />

      <AnimatePresence mode="wait">

        <div key={page}>

          {page === 'home' && (
            <Home go={go} />
          )}

          {page === 'login' && (
            <Auth
              mode="login"
              go={go}
              setUser={setUser}
            />
          )}

          {page === 'register' && (
            <Auth
              mode="register"
              go={go}
              setUser={setUser}
            />
          )}

          {page === 'track' && (
            <Track />
          )}

          {page === 'submit' && (
            <Submit
              user={user}
              go={go}
            />
          )}

          {page === 'citizen' && user && (
            <Citizen
              user={user}
              go={go}
            />
          )}

          {page === 'admin' && user && (
            <Admin />
          )}

        </div>

      </AnimatePresence>
    </>
  );
}


/* =========================================================
   START REACT
========================================================= */

createRoot(
  document.getElementById('root')
).render(
  <App />
);
