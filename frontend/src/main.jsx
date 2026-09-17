function Submit({ user, go }) {
  const [form, setForm] = useState({
    title: '',
    description: '',
    category: '1',
    priority: 'MEDIUM',
    location: ''
  });

  const [msg, setMsg] = useState('');
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);

  const categories = [
    { id: 1, name: 'Roads' },
    { id: 2, name: 'Street Lights' },
    { id: 3, name: 'Garbage' },
    { id: 4, name: 'Water Supply' },
    { id: 5, name: 'Electricity' },
    { id: 6, name: 'Drainage' },
    { id: 7, name: 'Public Health' },
    { id: 8, name: 'Public Transport' }
  ];

  const send = async (e) => {
    e.preventDefault();

    if (!user) {
      go('login');
      return;
    }

    setLoading(true);
    setMsg('');
    setSuccess(false);

    try {
      const response = await fetch(API + '/complaints', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization':
            'Bearer ' + localStorage.getItem('token')
        },
        body: JSON.stringify({
          title: form.title,
          description: form.description,
          priority: form.priority,
          location: form.location,
          citizen: {
            id: user.userId
          },
          category: {
            id: Number(form.category)
          }
        })
      });

      const responseText = await response.text();

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

      setSuccess(true);

      setMsg(
        'Complaint submitted successfully! Tracking number: ' +
        data.trackingNumber
      );

      setForm({
        title: '',
        description: '',
        category: '1',
        priority: 'MEDIUM',
        location: ''
      });

    } catch (error) {
      console.error('Complaint submission error:', error);

      setSuccess(false);
      setMsg(
        error.message ||
        'Unable to submit complaint.'
      );

    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="max-w-3xl mx-auto px-5 pt-14 pb-20">

      <div className="mb-8">
        <p className="text-sm font-semibold text-blue-400 uppercase tracking-widest">
          CivicResolve
        </p>

        <h2 className="text-4xl md:text-5xl font-black mt-2">
          Report a public issue
        </h2>

        <p className="text-slate-400 mt-3 text-lg">
          Provide enough detail for an authority to act quickly.
        </p>
      </div>

      <form
        onSubmit={send}
        className="glass rounded-3xl p-6 md:p-8 space-y-5"
      >

        <div>
          <label className="block text-sm font-semibold text-slate-300 mb-2">
            Complaint title
          </label>

          <input
            className="input"
            placeholder="Example: Large pothole near main road"
            required
            value={form.title}
            onChange={e =>
              setForm({
                ...form,
                title: e.target.value
              })
            }
          />
        </div>

        <div>
          <label className="block text-sm font-semibold text-slate-300 mb-2">
            Description
          </label>

          <textarea
            className="input min-h-40 resize-y"
            placeholder="Describe the issue, what happened and where..."
            required
            value={form.description}
            onChange={e =>
              setForm({
                ...form,
                description: e.target.value
              })
            }
          />
        </div>

        <div className="grid md:grid-cols-2 gap-4">

          <div>
            <label className="block text-sm font-semibold text-slate-300 mb-2">
              Category
            </label>

            <select
              className="input"
              value={form.category}
              onChange={e =>
                setForm({
                  ...form,
                  category: e.target.value
                })
              }
            >
              {categories.map(category => (
                <option
                  key={category.id}
                  value={category.id}
                >
                  {category.name}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-semibold text-slate-300 mb-2">
              Priority
            </label>

            <select
              className="input"
              value={form.priority}
              onChange={e =>
                setForm({
                  ...form,
                  priority: e.target.value
                })
              }
            >
              {['LOW', 'MEDIUM', 'HIGH', 'URGENT'].map(priority => (
                <option
                  key={priority}
                  value={priority}
                >
                  {priority}
                </option>
              ))}
            </select>
          </div>

        </div>

        <div>
          <label className="block text-sm font-semibold text-slate-300 mb-2">
            Location
          </label>

          <input
            className="input"
            placeholder="Location / landmark"
            value={form.location}
            onChange={e =>
              setForm({
                ...form,
                location: e.target.value
              })
            }
          />
        </div>

        <button
          type="submit"
          disabled={loading}
          className="
            w-full
            rounded-xl
            bg-gradient-to-r
            from-blue-500
            to-violet-500
            text-white
            px-5
            py-3.5
            font-bold
            shadow-lg
            shadow-blue-500/20
            hover:scale-[1.01]
            hover:shadow-blue-500/30
            transition
            disabled:opacity-50
            disabled:cursor-not-allowed
            disabled:hover:scale-100
          "
        >
          {loading ? 'Submitting...' : 'Submit grievance'}
        </button>

        {msg && (
          <div
            className={`rounded-xl px-4 py-3 text-sm font-medium ${
              success
                ? 'bg-emerald-500/10 border border-emerald-400/20 text-emerald-300'
                : 'bg-red-500/10 border border-red-400/20 text-red-300'
            }`}
          >
            {msg}
          </div>
        )}

      </form>
    </section>
  );
}
